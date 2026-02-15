package br.ufes.catalogo.service;

import br.ufes.catalogo.dto.LoginDTO;
import br.ufes.catalogo.dto.TokenDTO;
import br.ufes.catalogo.model.Usuario;
import br.ufes.catalogo.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mindrot.jbcrypt.BCrypt;

import java.lang.reflect.Field;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    private AuthService authService;

    private Usuario usuario;
    private LoginDTO loginDTO;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);

        authService = new AuthService();
        injetarDependencia(authService, "usuarioRepository", usuarioRepository);

        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setUsername("testuser");
        usuario.setEmail("test@example.com");
        usuario.setSenha(BCrypt.hashpw("senha123", BCrypt.gensalt()));
        usuario.setAtivo(true);

        loginDTO = new LoginDTO("testuser", "senha123");
    }

    private static void injetarDependencia(Object target, String fieldName, Object value) throws Exception {
        Field f = target.getClass().getDeclaredField(fieldName);
        f.setAccessible(true);
        f.set(target, value);
    }

    @Test
    void deveRegistrarUsuarioComSucesso() {
        Usuario novoUsuario = new Usuario();
        novoUsuario.setUsername("newuser");
        novoUsuario.setEmail("new@example.com");
        novoUsuario.setSenha("senha123");

        when(usuarioRepository.buscarPorUsername(anyString())).thenReturn(Optional.empty());
        when(usuarioRepository.buscarPorEmail(anyString())).thenReturn(Optional.empty());
        when(usuarioRepository.salvar(any(Usuario.class))).thenReturn(novoUsuario);

        Usuario resultado = authService.registrar(novoUsuario);

        assertNotNull(resultado);
        verify(usuarioRepository, times(1)).salvar(any(Usuario.class));
    }

    @Test
    void deveLancarExcecaoQuandoUsernameJaExiste() {
        when(usuarioRepository.buscarPorUsername(anyString())).thenReturn(Optional.of(usuario));

        Usuario novoUsuario = new Usuario();
        novoUsuario.setUsername("testuser");
        novoUsuario.setEmail("other@example.com");
        novoUsuario.setSenha("senha123");

        assertThrows(RuntimeException.class, () -> authService.registrar(novoUsuario));
    }

    @Test
    void deveFazerLoginComSucesso() {
        when(usuarioRepository.buscarPorUsername("testuser")).thenReturn(Optional.of(usuario));
        when(usuarioRepository.salvar(any(Usuario.class))).thenReturn(usuario);

        TokenDTO token = authService.login(loginDTO);

        assertNotNull(token);
        assertNotNull(token.getToken());
        assertEquals("Bearer", token.getTipo());
    }

    @Test
    void deveLancarExcecaoQuandoCredenciaisInvalidas() {
        when(usuarioRepository.buscarPorUsername(anyString())).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> authService.login(loginDTO));
        assertTrue(ex.getMessage().toLowerCase().contains("não encontrado"));
    }

    @Test
    void deveLancarExcecaoQuandoSenhaIncorreta() {
        when(usuarioRepository.buscarPorUsername("testuser")).thenReturn(Optional.of(usuario));

        LoginDTO loginErrado = new LoginDTO("testuser", "senhaerrada");

        RuntimeException ex = assertThrows(RuntimeException.class, () -> authService.login(loginErrado));
        assertTrue(ex.getMessage().toLowerCase().contains("senha"));
    }
}
