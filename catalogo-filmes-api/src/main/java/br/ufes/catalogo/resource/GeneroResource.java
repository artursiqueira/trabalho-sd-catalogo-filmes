package br.ufes.catalogo.resource;

import br.ufes.catalogo.dto.FilmeDTO;
import br.ufes.catalogo.dto.GeneroDTO;
import br.ufes.catalogo.model.Genero;
import br.ufes.catalogo.repository.GeneroRepository;
import br.ufes.catalogo.security.Secured;
import br.ufes.catalogo.service.FilmeService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/generos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class GeneroResource {

    private final GeneroRepository generoRepository = new GeneroRepository();
    private final FilmeService filmeService = new FilmeService();

    /**
     * GET /generos - Lista todos os gêneros
     */
    @GET
    public Response listar() {
        List<GeneroDTO> generos = generoRepository.listarTodos().stream()
                .map(GeneroDTO::new)
                .toList();
        return Response.ok(generos).build();
    }

    /**
     * GET /generos/{id} - Busca um gênero específico
     */
    @GET
    @Path("/{id}")
    public Response buscarPorId(@PathParam("id") Long id) {
        Genero genero = generoRepository.buscarPorId(id)
                .orElseThrow(() -> new NotFoundException("Gênero não encontrado"));
        return Response.ok(new GeneroDTO(genero)).build();
    }

    /**
     * GET /generos/{id}/filmes - Lista filmes de um gênero
     */
    @GET
    @Path("/{id}/filmes")
    public Response listarFilmesPorGenero(@PathParam("id") Long id) {
        List<FilmeDTO> filmes = filmeService.buscarPorGenero(id);
        return Response.ok(filmes).build();
    }

    /**
     * POST /generos - Cria um novo gênero (requer autenticação)
     */
    @POST
    @Secured
    public Response criar(Genero genero) {
        Genero generoCriado = generoRepository.salvar(genero);
        return Response.status(Response.Status.CREATED)
                .entity(new GeneroDTO(generoCriado))
                .build();
    }

    /**
     * PUT /generos/{id} - Atualiza um gênero (requer autenticação)
     */
    @PUT
    @Path("/{id}")
    @Secured
    public Response atualizar(@PathParam("id") Long id, Genero genero) {
        if (generoRepository.buscarPorId(id).isEmpty()) {
            throw new NotFoundException("Gênero não encontrado");
        }
        genero.setId(id);
        Genero generoAtualizado = generoRepository.salvar(genero);
        return Response.ok(new GeneroDTO(generoAtualizado)).build();
    }

    /**
     * DELETE /generos/{id} - Remove um gênero (requer autenticação)
     */
    @DELETE
    @Path("/{id}")
    @Secured
    public Response deletar(@PathParam("id") Long id) {
        if (generoRepository.buscarPorId(id).isEmpty()) {
            throw new NotFoundException("Gênero não encontrado");
        }
        generoRepository.deletar(id);
        return Response.noContent().build();
    }
}
