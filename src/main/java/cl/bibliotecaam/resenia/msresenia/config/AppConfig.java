package cl.bibliotecaam.resenia.msresenia.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

public class AppConfig {
    @Value("${usuario.url}")
    private String usuarioUrl;

    @Bean
    public WebClient webClientUsuario(){
        return WebClient.builder()
                .baseUrl(usuarioUrl)
                .build();
    }

    @Value("${libro.url}")
    private String libroUrl;

    @Bean
    public WebClient webClientLibro(){
        return WebClient.builder()
                .baseUrl(libroUrl)
                .build();
    }
}