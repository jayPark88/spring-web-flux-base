package com.jaypark8282.webFlux.api.v1.sample.controller;

import com.jaypark8282.webFlux.api.v1.sample.dto.UserDto;
import com.jaypark8282.webFlux.api.v1.sample.service.SampleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/v1/sample")
@RequiredArgsConstructor
public class SampleController {

    private final SampleService service;

    @GetMapping("/mono/random-user")
    public Mono<UserDto> hello() {
        return service.getRandomUser();
    }
}
