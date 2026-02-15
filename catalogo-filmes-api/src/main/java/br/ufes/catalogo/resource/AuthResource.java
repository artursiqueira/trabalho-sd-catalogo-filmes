package br.ufes.catalogo.resource;

import br.ufes.catalogo.dto.LoginDTO;
import br.ufes.catalogo.dto.TokenDTO;
import br.ufes.catalogo.model.Usuario;
import br.ufes.catalogo.service.AuthService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.HashMap;
import java.util.Map;

@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthResource {

    private AuthService authService = new AuthService();

    /**
     * POST /auth/registrar - Registra um novo usuário
     */
    @POST
    @Path("/registrar")
    public Response registrar(Usuario usuario) {
        try {
            Usuario usuarioCriado = authService.registrar(usuario);
            
            Map<String, Object> response = new HashMap<>();
            response.put("id", usuarioCriado.getId());
            response.put("username", usuarioCriado.getUsername());
            response.put("email", usuarioCriado.getEmail());
            response.put("mensagem", "Usuário registrado com sucesso");
            
            return Response.status(Response.Status.CREATED)
                    .entity(response)
                    .build();
        } catch (RuntimeException e) {
            Map<String, Object> erro = new HashMap<>();
            erro.put("erro", "Erro ao registrar usuário");
            erro.put("mensagem", e.getMessage());
            erro.put("status", 400);
            
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(erro)
                    .build();
        }
    }

    /**
     * POST /auth/login - Realiza login e retorna token JWT
     */
    @POST
    @Path("/login")
    public Response login(LoginDTO loginDTO) {
        try {
            TokenDTO token = authService.login(loginDTO);
            return Response.ok(token).build();
        } catch (RuntimeException e) {
            Map<String, Object> erro = new HashMap<>();
            erro.put("erro", "Erro ao fazer login");
            erro.put("mensagem", e.getMessage());
            erro.put("status", 401);
            
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(erro)
                    .build();
        }
    }
}
