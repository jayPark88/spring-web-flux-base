package com.jaypark8282.webFlux.api.v1.sample.controller;

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

    @GetMapping("/mono")
    public Mono<String> hello() {
        return Mono.just("hello");
    }
}
