package br.ufes.catalogo.service;

import br.ufes.catalogo.dto.LoginDTO;
import br.ufes.catalogo.dto.TokenDTO;
import br.ufes.catalogo.model.Usuario;
import br.ufes.catalogo.repository.UsuarioRepository;
import br.ufes.catalogo.security.JwtUtil;
import org.mindrot.jbcrypt.BCrypt;

public class AuthService {

    private UsuarioRepository usuarioRepository = new UsuarioRepository();

    public Usuario registrar(Usuario usuario) {
        // Verifica se username já existe
        if (usuarioRepository.buscarPorUsername(usuario.getUsername()).isPresent()) {
            throw new RuntimeException("Username já existe");
        }

        // Verifica se email já existe
        if (usuarioRepository.buscarPorEmail(usuario.getEmail()).isPresent()) {
            throw new RuntimeException("Email já existe");
        }

        // Criptografa a senha
        String senhaCriptografada = BCrypt.hashpw(usuario.getSenha(), BCrypt.gensalt(12));
        usuario.setSenha(senhaCriptografada);

        return usuarioRepository.salvar(usuario);
    }

    public TokenDTO login(LoginDTO loginDTO) {
        Usuario usuario = usuarioRepository.buscarPorUsername(loginDTO.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (!usuario.getAtivo()) {
            throw new RuntimeException("Usuário inativo");
        }

        if (!BCrypt.checkpw(loginDTO.getSenha(), usuario.getSenha())) {
            throw new RuntimeException("Senha incorreta");
        }

        String token = JwtUtil.gerarToken(usuario);

        // Atualiza último acesso
        usuario.setUltimoAcesso(java.time.LocalDateTime.now());
        usuarioRepository.salvar(usuario);

        return new TokenDTO(token, System.currentTimeMillis() + 86400000);
    }
}
