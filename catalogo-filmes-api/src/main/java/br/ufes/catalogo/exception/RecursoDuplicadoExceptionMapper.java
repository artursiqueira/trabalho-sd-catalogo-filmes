package br.ufes.catalogo.exception;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import java.util.HashMap;
import java.util.Map;

@Provider
public class RecursoDuplicadoExceptionMapper implements ExceptionMapper<RecursoDuplicadoException> {
    
    @Override
    public Response toResponse(RecursoDuplicadoException exception) {
        Map<String, Object> erro = new HashMap<>();
        erro.put("erro", "Recurso duplicado");
        erro.put("mensagem", exception.getMessage());
        erro.put("status", 409);
        
        return Response.status(Response.Status.CONFLICT)
                .entity(erro)
                .build();
    }
}
