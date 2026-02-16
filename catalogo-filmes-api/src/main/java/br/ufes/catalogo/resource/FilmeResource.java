package br.ufes.catalogo.resource;

import br.ufes.catalogo.dto.FilmeDTO;
import br.ufes.catalogo.dto.PaginacaoDTO;
import br.ufes.catalogo.security.Secured;
import br.ufes.catalogo.service.FilmeService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/filmes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class FilmeResource {

    private FilmeService filmeService = new FilmeService();

    @GET
    public Response listar(
            @QueryParam("pagina") @DefaultValue("0") int pagina,
            @QueryParam("tamanho") @DefaultValue("10") int tamanho,
            @QueryParam("titulo") String titulo,
            @QueryParam("generoId") Long generoId,
            @QueryParam("diretor") String diretor) {
        
        if (titulo != null && !titulo.isEmpty()) {
            List<FilmeDTO> filmes = filmeService.buscarPorTitulo(titulo);
            return Response.ok(filmes).build();
        }
        
        if (generoId != null) {
            List<FilmeDTO> filmes = filmeService.buscarPorGenero(generoId);
            return Response.ok(filmes).build();
        }

        if (diretor != null && !diretor.isEmpty()) {
            List<FilmeDTO> filmes = filmeService.buscarPorDiretor(diretor);
            return Response.ok(filmes).build();
        }

        if (tamanho > 0) {
            PaginacaoDTO<FilmeDTO> resultado = filmeService.listarPaginado(pagina, tamanho);
            return Response.ok(resultado).build();
        }

        List<FilmeDTO> filmes = filmeService.listarTodos();
        return Response.ok(filmes).build();
    }

    @GET
    @Path("/{id}")
    public Response buscarPorId(@PathParam("id") Long id) {
        FilmeDTO filme = filmeService.buscarPorId(id);
        return Response.ok(filme).build();
    }

    @POST
    @Secured
    public Response criar(FilmeDTO dto) {
        FilmeDTO filmeCriado = filmeService.criar(dto);
        return Response.status(Response.Status.CREATED)
                .entity(filmeCriado)
                .build();
    }

    @PUT
    @Path("/{id}")
    @Secured
    public Response atualizar(@PathParam("id") Long id, FilmeDTO dto) {
        FilmeDTO filmeAtualizado = filmeService.atualizar(id, dto);
        return Response.ok(filmeAtualizado).build();
    }

    @PATCH
    @Path("/{id}")
    @Secured
    public Response atualizarParcial(@PathParam("id") Long id, FilmeDTO dto) {
        FilmeDTO filmeAtualizado = filmeService.atualizar(id, dto);
        return Response.ok(filmeAtualizado).build();
    }

    @DELETE
    @Path("/{id}")
    @Secured
    public Response deletar(@PathParam("id") Long id) {
        filmeService.deletar(id);
        return Response.noContent().build();
    }
}
