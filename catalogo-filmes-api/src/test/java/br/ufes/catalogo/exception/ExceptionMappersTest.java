package br.ufes.catalogo.exception;

import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotAllowedException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ExceptionMappersTest {

    @Test
    void recursoNaoEncontradoMapperDeveRetornar404() {
        RecursoNaoEncontradoExceptionMapper mapper = new RecursoNaoEncontradoExceptionMapper();
        Response r = mapper.toResponse(new RecursoNaoEncontradoException("x"));
        assertEquals(404, r.getStatus());
        assertTrue(((Map<?, ?>) r.getEntity()).containsKey("mensagem"));
    }

    @Test
    void notFoundMapperDeveRetornar404() {
        NotFoundExceptionMapper mapper = new NotFoundExceptionMapper();
        Response r = mapper.toResponse(new NotFoundException("nf"));
        assertEquals(404, r.getStatus());
    }

    @Test
    void methodNotAllowedMapperDeveRetornar405() {
        MethodNotAllowedExceptionMapper mapper = new MethodNotAllowedExceptionMapper();
        Response r = mapper.toResponse(new NotAllowedException("na"));
        assertEquals(405, r.getStatus());
    }

    @Test
    void badRequestMapperDeveRetornar400() {
        BadRequestExceptionMapper mapper = new BadRequestExceptionMapper();
        Response r = mapper.toResponse(new BadRequestException("br"));
        assertEquals(400, r.getStatus());
    }

    @Test
    void illegalArgumentMapperDeveRetornar400() {
        IllegalArgumentExceptionMapper mapper = new IllegalArgumentExceptionMapper();
        Response r = mapper.toResponse(new IllegalArgumentException("ia"));
        assertEquals(400, r.getStatus());
    }

    @Test
    void genericMapperComRuntimeExceptionDeveRetornar500ComJson() {
        GenericExceptionMapper mapper = new GenericExceptionMapper();
        Response r = mapper.toResponse(new RuntimeException("boom"));
        assertEquals(500, r.getStatus());
        Map<?, ?> entity = (Map<?, ?>) r.getEntity();
        assertEquals(500, entity.get("status"));
    }

    @Test
    void genericMapperComWebApplicationExceptionPreservaResponse() {
        GenericExceptionMapper mapper = new GenericExceptionMapper();
        NotFoundException ex = new NotFoundException("nf");
        Response r = mapper.toResponse(ex);
        assertEquals(404, r.getStatus());
    }
}
