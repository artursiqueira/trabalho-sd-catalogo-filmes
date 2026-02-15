package br.ufes.catalogo.exception;

import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.util.HashMap;
import java.util.Map;

@Provider
public class BadRequestExceptionMapper implements ExceptionMapper<BadRequestException> {
    @Override
    public Response toResponse(BadRequestException exception) {
        Map<String, Object> erro = new HashMap<>();
        erro.put("erro", "Requisição inválida");
        erro.put("mensagem", exception.getMessage());
        erro.put("status", 400);

        return Response.status(Response.Status.BAD_REQUEST)
                .entity(erro)
                .build();
    }
}
