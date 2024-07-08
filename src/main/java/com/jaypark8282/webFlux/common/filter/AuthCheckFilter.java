package com.jaypark8282.webFlux.common.filter;

import com.fasterxml.jackson.databind.JsonNode;
import com.jaypark8282.webFlux.common.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import static com.jaypark8282.webFlux.common.eums.ResponseErrorCode.FAIL_401;
import static com.jaypark8282.webFlux.common.eums.ResponseErrorCode.FAIL_500;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthCheckFilter implements WebFilter {

    private final WebClient webClient;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();

        if (path.equals("/v1/auth/authorize")) {
            return chain.filter(exchange);
        }

        String authToken = exchange.getRequest().getHeaders().getFirst("Authorization");

        if (authToken == null || authToken.isEmpty()) {
            exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
            return exchange.getResponse().setComplete();
        }

        return authCheckInterfacing(authToken)
                .flatMap(isAuthorized -> {
                    if (Boolean.TRUE.equals(isAuthorized)) {
                        return chain.filter(exchange);
                    } else {
                        exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                        return exchange.getResponse().setComplete();
                    }
                });
    }

    private Mono<Boolean> authCheckInterfacing(String authToken) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                                .path("client/v1/sample")
                                .queryParam("nat", "u").build()
                        )
                .header("Authorization", authToken)
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
                .doOnNext(response -> {
                    // 성공적인 응답이 도착했을 때의 로깅
                    log.info("AuthCheck Request successful. Response: " + response.toString());
                })
                .flatMap(this::extractTokenFromResponse);
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

    private Mono<Boolean> extractTokenFromResponse(JsonNode jsonNode) {
        JsonNode authNode = jsonNode.get("result");  // data 필드에서 token 필드를 추출
        boolean authCheckResult = Boolean.parseBoolean(authNode.asText());  // token 값을 문자열로 변환
        return Mono.just(authCheckResult);
    }
}
