package br.ufes.catalogo.security;

import jakarta.annotation.Priority;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;
import java.util.HashMap;
import java.util.Map;

@Provider
@Secured
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter {

    @Override
    public void filter(ContainerRequestContext requestContext) {
        String authorizationHeader = requestContext.getHeaderString("Authorization");

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            abortWithUnauthorized(requestContext, "Token de autorização ausente ou inválido");
            return;
        }

        String token = authorizationHeader.substring("Bearer".length()).trim();

        try {
            // Valida o token
            JwtUtil.validarToken(token);
            
            // Adiciona informações do usuário no contexto
            String username = JwtUtil.getUsernameFromToken(token);
            requestContext.setProperty("username", username);
            requestContext.setProperty("userId", JwtUtil.getUserIdFromToken(token));
            
        } catch (Exception e) {
            abortWithUnauthorized(requestContext, "Token inválido ou expirado");
        }
    }

    private void abortWithUnauthorized(ContainerRequestContext requestContext, String mensagem) {
        Map<String, Object> erro = new HashMap<>();
        erro.put("erro", "Não autorizado");
        erro.put("mensagem", mensagem);
        erro.put("status", 401);
        
        requestContext.abortWith(
            Response.status(Response.Status.UNAUTHORIZED)
                .entity(erro)
                .build()
        );
    }
}
