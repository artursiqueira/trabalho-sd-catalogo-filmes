package br.ufes.catalogo.security;

import br.ufes.catalogo.model.Usuario;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    @Test
    void deveGerarEValidarTokenComSucesso() {
        Usuario usuario = new Usuario();
        usuario.setId(10L);
        usuario.setUsername("joao");
        usuario.setTipo(Usuario.TipoUsuario.USUARIO);

        String token = JwtUtil.gerarToken(usuario);
        assertNotNull(token);
        assertTrue(JwtUtil.isTokenValido(token));

        assertEquals("joao", JwtUtil.getUsernameFromToken(token));
        assertEquals(10L, JwtUtil.getUserIdFromToken(token));
    }

    @Test
    void tokenInvalidoDeveRetornarFalseEmIsTokenValido() {
        assertFalse(JwtUtil.isTokenValido("token.invalido"));
    }

    @Test
    void validarTokenInvalidoDeveLancarRuntimeException() {
        RuntimeException ex = assertThrows(RuntimeException.class, () -> JwtUtil.validarToken("token.invalido"));
        assertTrue(ex.getMessage().toLowerCase().contains("token"));
    }
}
