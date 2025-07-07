package com.major.global;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
    @Bean
    @Qualifier("storage")
    public WebClient webClientStorage() {
        return WebClient.builder()
                .baseUrl("http://localhost:9000")
                .build();
    }
}
