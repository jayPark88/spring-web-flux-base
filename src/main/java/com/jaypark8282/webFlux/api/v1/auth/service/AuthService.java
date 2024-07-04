package com.jaypark8282.webFlux.api.v1.auth.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.jaypark8282.webFlux.api.v1.auth.dto.LoginDto;
import com.jaypark8282.webFlux.common.exception.CustomException;
import com.jaypark8282.webFlux.common.response.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static com.jaypark8282.webFlux.common.eums.ResponseErrorCode.FAIL_401;
import static com.jaypark8282.webFlux.common.eums.ResponseErrorCode.FAIL_500;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final WebClient webClient;

    public Mono<String> authorize(LoginDto loginDto) {
        return webClient.post()
                .uri("client/v1/auth/login")
                .bodyValue(loginDto)
                .retrieve()
                .onStatus(
                        HttpStatusCode::is4xxClientError,
                        this::handle4xxError
                )
                .onStatus(
                        HttpStatusCode::is5xxServerError,
                        this::handle5xxError
                )
                .bodyToMono(JsonNode.class)
                .flatMap(this::extractTokenFromResponse);  // 비즈니스 로직을 private 메서드로 분리
    }

    private Mono<? extends Throwable> handle4xxError(ClientResponse clientResponse) {
        return clientResponse.bodyToMono(String.class)
                .flatMap(errorMessage -> Mono.error(new CustomException(
                        FAIL_401.code(),
                        errorMessage,
                        HttpStatus.UNAUTHORIZED)));
    }

    private Mono<? extends Throwable> handle5xxError(ClientResponse clientResponse) {
        return clientResponse.bodyToMono(String.class)
                .flatMap(errorMessage -> Mono.error(new CustomException(
                        FAIL_500.code(),
                        errorMessage,
                        HttpStatus.INTERNAL_SERVER_ERROR)));
    }

    private Mono<String> extractTokenFromResponse(JsonNode jsonNode) {
        JsonNode tokenNode = jsonNode.get("data").get("token");  // data 필드에서 token 필드를 추출
        if (tokenNode != null) {
            String token = tokenNode.asText();  // token 값을 문자열로 변환
            return Mono.just(token);
        } else {
            return Mono.error(new RuntimeException("Token not found in response"));
        }
    }
}
