package com.jaypark8282.webFlux.api.v1.user.controller;

import com.jaypark8282.webFlux.api.v1.user.dto.request.UserDto;
import com.jaypark8282.webFlux.api.v1.user.dto.response.SignUpResponseDto;
import com.jaypark8282.webFlux.api.v1.user.service.UserService;
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
@RequestMapping("/v1/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final MessageSource messageSource;

    @PostMapping("/signUp")
    public Mono<SignUpResponseDto> signUp(@RequestBody UserDto userDto) {

        return userService.signUp(userDto)
                .doOnNext(el -> {
                    // 성공적인 응답을 받을 때 로그 남기기
                    log.info("signUp successful for user: {}", el.getResult());
                })
                .doOnError(e -> {
                    // 오류가 발생했을 때 로그 남기기
                    log.error("signUp failed Error: {}", e.getMessage());
                })
                .onErrorResume(e -> Mono.error(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                        messageSource.getMessage("user.signup.save.fail", null, Locale.getDefault()))));
    }
}
