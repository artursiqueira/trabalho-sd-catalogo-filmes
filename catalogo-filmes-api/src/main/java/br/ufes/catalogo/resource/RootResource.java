package br.ufes.catalogo.resource;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * Permite que acessar /api redirecione para a página index.html (guia de rotas).
 */
@Path("/")
public class RootResource {

    @GET
    @Produces(MediaType.TEXT_HTML)
    public Response redirectToIndex() {
        return Response.seeOther(java.net.URI.create("/index.html")).build();
    }
}
