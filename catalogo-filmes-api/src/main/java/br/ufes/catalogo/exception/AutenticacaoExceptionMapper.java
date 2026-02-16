package br.ufes.catalogo.exception;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import java.util.HashMap;
import java.util.Map;

@Provider
public class AutenticacaoExceptionMapper implements ExceptionMapper<AutenticacaoException> {
    
    @Override
    public Response toResponse(AutenticacaoException exception) {
        Map<String, Object> erro = new HashMap<>();
        erro.put("erro", "Falha na autenticação");
        erro.put("mensagem", exception.getMessage());
        erro.put("status", 401);
        
        return Response.status(Response.Status.UNAUTHORIZED)
                .entity(erro)
                .build();
    }
}
