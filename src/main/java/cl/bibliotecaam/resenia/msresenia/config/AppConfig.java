package cl.bibliotecaam.resenia.msresenia.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
@Configuration
public class AppConfig {
    @Value("${usuario.url}")
    private String usuarioUrl;

    @Value("${libro.url}")
    private String libroUrl;

    @Bean
    public WebClient webClientUsuario(){
        return WebClient.builder()
                .baseUrl(usuarioUrl)
                .build();
    }

    @Bean
    public WebClient webClientLibro(){
        return WebClient.builder()
                .baseUrl(libroUrl)
                .build();
    }
}