package br.ufes.catalogo.resource;

import br.ufes.catalogo.dto.FilmeDTO;
import br.ufes.catalogo.dto.GeneroDTO;
import br.ufes.catalogo.model.Genero;
import br.ufes.catalogo.security.Secured;
import br.ufes.catalogo.service.FilmeService;
import br.ufes.catalogo.service.GeneroService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/generos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class GeneroResource {

    private final GeneroService generoService = new GeneroService();
    private final FilmeService filmeService = new FilmeService();

    @GET
    public Response listar() {
        List<GeneroDTO> generos = generoService.listarTodos();
        return Response.ok(generos).build();
    }

    @GET
    @Path("/{id}")
    public Response buscarPorId(@PathParam("id") Long id) {
        GeneroDTO genero = generoService.buscarPorId(id);
        return Response.ok(genero).build();
    }

    @GET
    @Path("/{id}/filmes")
    public Response listarFilmesPorGenero(@PathParam("id") Long id) {
        generoService.buscarPorId(id);
        
        List<FilmeDTO> filmes = filmeService.buscarPorGenero(id);
        return Response.ok(filmes).build();
    }

    @POST
    @Secured
    public Response criar(Genero genero) {
        GeneroDTO generoCriado = generoService.criar(genero);
        return Response.status(Response.Status.CREATED)
                .entity(generoCriado)
                .build();
    }

    @PUT
    @Path("/{id}")
    @Secured
    public Response atualizar(@PathParam("id") Long id, Genero genero) {
        GeneroDTO generoAtualizado = generoService.atualizar(id, genero);
        return Response.ok(generoAtualizado).build();
    }

    @DELETE
    @Path("/{id}")
    @Secured
    public Response deletar(@PathParam("id") Long id) {
        generoService.deletar(id);
        return Response.noContent().build();
    }
}
