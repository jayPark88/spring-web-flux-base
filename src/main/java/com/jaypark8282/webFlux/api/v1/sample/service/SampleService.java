package com.jaypark8282.webFlux.api.v1.sample.service;

import com.jaypark8282.webFlux.api.v1.sample.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class SampleService {
    private final WebClient webClient;

    //randomUser 조회
    public Mono<UserDto> getRandomUser(){
        String url = "api";
        return webClient.get().uri(url).retrieve().bodyToMono(UserDto.class);
    }
}
