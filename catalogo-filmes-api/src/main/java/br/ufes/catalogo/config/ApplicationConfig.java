package br.ufes.catalogo.config;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ServerProperties;

public class ApplicationConfig extends ResourceConfig {

    public ApplicationConfig() {
        packages("br.ufes.catalogo.resource");
        packages("br.ufes.catalogo.exception");
        packages("br.ufes.catalogo.security");

        register(JacksonFeature.class);

        register(CorsFilter.class);
    }
}
