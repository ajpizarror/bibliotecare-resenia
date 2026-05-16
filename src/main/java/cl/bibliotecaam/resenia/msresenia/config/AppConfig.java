package cl.bibliotecaam.resenia.msresenia.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

public class AppConfig {
    @Value("${usuario.url}")

    @Bean
    public WebClient webClient(){
        return WebClient.builder()
                .build();
    }
}
