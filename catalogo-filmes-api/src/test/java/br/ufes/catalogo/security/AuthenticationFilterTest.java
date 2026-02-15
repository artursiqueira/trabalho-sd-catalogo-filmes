package br.ufes.catalogo.security;

import br.ufes.catalogo.model.Usuario;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AuthenticationFilterTest {

    @Test
    void semHeaderAuthorizationDeveAbortarCom401() {
        ContainerRequestContext ctx = mock(ContainerRequestContext.class);
        when(ctx.getHeaderString("Authorization")).thenReturn(null);

        AuthenticationFilter filter = new AuthenticationFilter();
        filter.filter(ctx);

        verify(ctx, times(1)).abortWith(any(Response.class));
    }

    @Test
    void bearerSemTokenDeveAbortarCom401() {
        ContainerRequestContext ctx = mock(ContainerRequestContext.class);
        when(ctx.getHeaderString("Authorization")).thenReturn("Bearer ");

        AuthenticationFilter filter = new AuthenticationFilter();
        filter.filter(ctx);

        verify(ctx, times(1)).abortWith(any(Response.class));
    }

    @Test
    void tokenValidoDeveSetarPropriedadesNoContexto() {
        Usuario u = new Usuario();
        u.setId(1L);
        u.setUsername("maria");
        u.setTipo(Usuario.TipoUsuario.USUARIO);

        String token = JwtUtil.gerarToken(u);

        ContainerRequestContext ctx = mock(ContainerRequestContext.class);
        when(ctx.getHeaderString("Authorization")).thenReturn("Bearer " + token);

        AuthenticationFilter filter = new AuthenticationFilter();
        filter.filter(ctx);

        verify(ctx, never()).abortWith(any(Response.class));
        verify(ctx).setProperty("username", "maria");
        verify(ctx).setProperty("userId", 1L);
    }

    @Test
    void tokenInvalidoDeveAbortarCom401() {
        ContainerRequestContext ctx = mock(ContainerRequestContext.class);
        when(ctx.getHeaderString("Authorization")).thenReturn("Bearer token.invalido");

        AuthenticationFilter filter = new AuthenticationFilter();
        filter.filter(ctx);

        verify(ctx, times(1)).abortWith(any(Response.class));
    }
}
