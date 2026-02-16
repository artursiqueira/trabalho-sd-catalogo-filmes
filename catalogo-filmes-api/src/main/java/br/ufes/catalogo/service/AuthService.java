package br.ufes.catalogo.service;

import br.ufes.catalogo.dto.LoginDTO;
import br.ufes.catalogo.dto.TokenDTO;
import br.ufes.catalogo.exception.AutenticacaoException;
import br.ufes.catalogo.exception.DadosInvalidosException;
import br.ufes.catalogo.exception.RecursoDuplicadoException;
import br.ufes.catalogo.model.Usuario;
import br.ufes.catalogo.repository.UsuarioRepository;
import br.ufes.catalogo.security.JwtUtil;
import org.mindrot.jbcrypt.BCrypt;

public class AuthService {

    private UsuarioRepository usuarioRepository = new UsuarioRepository();

    public Usuario registrar(Usuario usuario) {
        validarDadosRegistro(usuario);
        
        if (usuarioRepository.buscarPorUsername(usuario.getUsername()).isPresent()) {
            throw new RecursoDuplicadoException("Username já existe: " + usuario.getUsername());
        }

        if (usuarioRepository.buscarPorEmail(usuario.getEmail()).isPresent()) {
            throw new RecursoDuplicadoException("Email já cadastrado: " + usuario.getEmail());
        }

        String senhaCriptografada = BCrypt.hashpw(usuario.getSenha(), BCrypt.gensalt(12));
        usuario.setSenha(senhaCriptografada);

        return usuarioRepository.salvar(usuario);
    }

    public TokenDTO login(LoginDTO loginDTO) {
        validarDadosLogin(loginDTO);
        
        Usuario usuario = usuarioRepository.buscarPorUsername(loginDTO.getUsername())
                .orElseThrow(() -> new AutenticacaoException("Credenciais inválidas"));

        if (!usuario.getAtivo()) {
            throw new AutenticacaoException("Usuário inativo. Entre em contato com o administrador");
        }

        if (!BCrypt.checkpw(loginDTO.getSenha(), usuario.getSenha())) {
            throw new AutenticacaoException("Credenciais inválidas");
        }

        String token = JwtUtil.gerarToken(usuario);

        usuario.setUltimoAcesso(java.time.LocalDateTime.now());
        usuarioRepository.salvar(usuario);

        return new TokenDTO(token, System.currentTimeMillis() + 86400000);
    }

    private void validarDadosRegistro(Usuario usuario) {
        if (usuario == null) {
            throw new DadosInvalidosException("Dados do usuário não podem ser nulos");
        }
        
        if (usuario.getUsername() == null || usuario.getUsername().trim().isEmpty()) {
            throw new DadosInvalidosException("Username é obrigatório");
        }
        
        if (usuario.getUsername().length() < 3 || usuario.getUsername().length() > 50) {
            throw new DadosInvalidosException("Username deve ter entre 3 e 50 caracteres");
        }
        
        if (usuario.getEmail() == null || usuario.getEmail().trim().isEmpty()) {
            throw new DadosInvalidosException("Email é obrigatório");
        }
        
        if (!usuario.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new DadosInvalidosException("Email inválido");
        }
        
        if (usuario.getSenha() == null || usuario.getSenha().isEmpty()) {
            throw new DadosInvalidosException("Senha é obrigatória");
        }
        
        if (usuario.getSenha().length() < 6) {
            throw new DadosInvalidosException("Senha deve ter pelo menos 6 caracteres");
        }
    }

    private void validarDadosLogin(LoginDTO loginDTO) {
        if (loginDTO == null) {
            throw new DadosInvalidosException("Dados de login não podem ser nulos");
        }
        
        if (loginDTO.getUsername() == null || loginDTO.getUsername().trim().isEmpty()) {
            throw new DadosInvalidosException("Username é obrigatório");
        }
        
        if (loginDTO.getSenha() == null || loginDTO.getSenha().isEmpty()) {
            throw new DadosInvalidosException("Senha é obrigatória");
        }
    }
}
