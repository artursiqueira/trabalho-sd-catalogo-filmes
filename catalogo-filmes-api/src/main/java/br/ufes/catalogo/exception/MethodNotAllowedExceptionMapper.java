package br.ufes.catalogo.exception;

import jakarta.ws.rs.NotAllowedException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.util.HashMap;
import java.util.Map;

@Provider
public class MethodNotAllowedExceptionMapper implements ExceptionMapper<NotAllowedException> {
    @Override
    public Response toResponse(NotAllowedException exception) {
        Map<String, Object> erro = new HashMap<>();
        erro.put("erro", "Método não permitido");
        erro.put("mensagem", exception.getMessage());
        erro.put("status", 405);

        return Response.status(Response.Status.METHOD_NOT_ALLOWED)
                .entity(erro)
                .build();
    }
}
