package br.ufes.catalogo.exception;

import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.util.HashMap;
import java.util.Map;

@Provider
public class NotFoundExceptionMapper implements ExceptionMapper<NotFoundException> {
    @Override
    public Response toResponse(NotFoundException exception) {
        // Se for 404 do container/servlet (ex.: recurso estático fora do Jersey),
        // não substitui a resposta padrão.
        Response r = exception.getResponse();
        if (r != null && r.getEntity() == null) {
            return r;
        }

        Map<String, Object> erro = new HashMap<>();
        erro.put("erro", "Recurso não encontrado");
        erro.put("mensagem", exception.getMessage());
        erro.put("status", 404);

        return Response.status(Response.Status.NOT_FOUND)
                .entity(erro)
                .build();
    }
}
