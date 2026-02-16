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

    @POST
    @Path("/registrar")
    public Response registrar(Usuario usuario) {
        Usuario usuarioCriado = authService.registrar(usuario);
        
        Map<String, Object> response = new HashMap<>();
        response.put("id", usuarioCriado.getId());
        response.put("username", usuarioCriado.getUsername());
        response.put("email", usuarioCriado.getEmail());
        response.put("mensagem", "Usuário registrado com sucesso");
        
        return Response.status(Response.Status.CREATED)
                .entity(response)
                .build();
    }

    @POST
    @Path("/login")
    public Response login(LoginDTO loginDTO) {
        TokenDTO token = authService.login(loginDTO);
        return Response.ok(token).build();
    }
}
