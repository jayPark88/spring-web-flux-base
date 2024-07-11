package com.jaypark8282.webFlux.common.filter;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class AuthCheckFilter implements WebFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        // 요청 URI 경로를 가져옵니다.
        String path = exchange.getRequest().getURI().getPath();

        // 요청 경로가 "/v1/auth/authorize"인 경우 필터 체인을 통과합니다.
        if (path.equals("/v1/auth/authorize")) {
            return chain.filter(exchange);
        }

        // "Authorization" 헤더의 첫 번째 값을 가져옵니다.
        return Mono.justOrEmpty(exchange.getRequest().getHeaders().getFirst("Authorization"))
                .flatMap(authToken -> {
                    // Authorization 헤더 값이 비어 있으면
                    if (authToken.isEmpty()) {
                        // 응답 상태 코드를 UNAUTHORIZED(401)로 설정하고 요청을 완료합니다.
                        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                        return exchange.getResponse().setComplete();
                    } else {
                        // Authorization 헤더 값이 존재하면 필터 체인을 통과합니다.
                        return chain.filter(exchange);
                    }
                })
                // Authorization 헤더가 없는 경우 실행됩니다, Mono.defer()는 switchIfEmpty의 조건이 충족될 때까지 실행을 지연 시킵니다.
                .switchIfEmpty(Mono.defer(() -> {
                    // 응답 상태 코드를 UNAUTHORIZED(401)로 설정하고 요청을 완료합니다.
                    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                    return exchange.getResponse().setComplete();
                }));
    }

}
