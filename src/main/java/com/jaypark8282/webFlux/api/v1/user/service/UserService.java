package com.jaypark8282.webFlux.api.v1.user.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jaypark8282.webFlux.api.v1.user.dto.request.UserDto;
import com.jaypark8282.webFlux.api.v1.user.dto.response.SignUpResponseDto;
import com.jaypark8282.webFlux.common.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static com.jaypark8282.webFlux.common.eums.ResponseErrorCode.FAIL_500;

@Service
@RequiredArgsConstructor
public class UserService {
    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    public Mono<SignUpResponseDto> signUp(UserDto userDto) {
        return webClient.post()
                .uri("client/v1/user/signUp")
                .bodyValue(userDto)
                .retrieve()
                .onStatus(
                        HttpStatusCode::is5xxServerError,
                        this::handle5xxError
                )
                .bodyToMono(JsonNode.class)
                .flatMap(this::extractTokenFromResponse);
    }

    private Mono<? extends Throwable> handle5xxError(ClientResponse clientResponse) {
        return clientResponse.bodyToMono(String.class)
                .flatMap(errorMessage -> Mono.error(new CustomException(
                        FAIL_500.code(),
                        errorMessage,
                        HttpStatus.INTERNAL_SERVER_ERROR)));
    }

    private Mono<SignUpResponseDto> extractTokenFromResponse(JsonNode jsonNode) {
        try {
            SignUpResponseDto signUpResponseDto = objectMapper.treeToValue(jsonNode, SignUpResponseDto.class);
            return Mono.just(signUpResponseDto);
        } catch (JsonProcessingException e) {
            throw new CustomException(FAIL_500.code(), e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
