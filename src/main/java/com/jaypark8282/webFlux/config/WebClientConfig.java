package com.jaypark8282.webFlux.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${service.interface.sample.url}")
    private String sampleOpenApiUrl;

    // webClient Bean 등록
    @Bean
    public WebClient webClient(WebClient.Builder builder){
        return builder
                .baseUrl(sampleOpenApiUrl).build();
    }

}
