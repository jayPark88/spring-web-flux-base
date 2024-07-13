package com.jaypark8282.webFlux.api.v1.auth.controller;

import com.jaypark8282.webFlux.api.v1.auth.dto.LoginDto;
import com.jaypark8282.webFlux.api.v1.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.util.Locale;

@Slf4j
@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final MessageSource messageSource;

    @PostMapping("/authorize")
    public Mono<String> authorize(@RequestBody LoginDto loginDto) {
        return authService.authorize(loginDto)
                .doOnNext(token -> {
                    // 성공적인 응답을 받을 때 로그 남기기
                    log.info("Authorization successful for user: {}", loginDto.getUserName());
                })
                .doOnError(e -> {
                    // 오류가 발생했을 때 로그 남기기
                    log.error("Authorization failed for user: {}. Error: {}", loginDto.getUserName(), e.getMessage());
                })
                .onErrorResume(e -> Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                        messageSource.getMessage("http.status.unauthorized", null, Locale.getDefault()))));
    }
}
