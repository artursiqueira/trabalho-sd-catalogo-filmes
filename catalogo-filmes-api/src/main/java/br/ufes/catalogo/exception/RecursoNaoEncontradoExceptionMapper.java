package br.ufes.catalogo.exception;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import java.util.HashMap;
import java.util.Map;

@Provider
public class RecursoNaoEncontradoExceptionMapper implements ExceptionMapper<RecursoNaoEncontradoException> {
    
    @Override
    public Response toResponse(RecursoNaoEncontradoException exception) {
        Map<String, Object> erro = new HashMap<>();
        erro.put("erro", "Recurso não encontrado");
        erro.put("mensagem", exception.getMessage());
        erro.put("status", 404);
        
        return Response.status(Response.Status.NOT_FOUND)
                .entity(erro)
                .build();
    }
}
