package br.ufes.catalogo.config;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ServerProperties;

public class ApplicationConfig extends ResourceConfig {

    public ApplicationConfig() {
        // Registra pacotes que contém os recursos REST
        packages("br.ufes.catalogo.resource");
        packages("br.ufes.catalogo.exception");
        packages("br.ufes.catalogo.security"); // Importante! Inclui AuthenticationFilter

        // Registra Jackson para serialização/deserialização JSON
        register(JacksonFeature.class);

        // Registra CORS filter
        register(CorsFilter.class);
    }
}
