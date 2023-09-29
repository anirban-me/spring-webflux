package com.vinsguru.webfluxdemo.controller;

import com.vinsguru.webfluxdemo.dto.MultiplyRequestDto;
import com.vinsguru.webfluxdemo.dto.Response;
import com.vinsguru.webfluxdemo.service.ReactiveMathService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("reactive-math")
public class ReactiveMathController {

    @Autowired
    private ReactiveMathService mathService;

    @GetMapping("square/{input}")
    public Mono<Response> findSquare(@PathVariable int input) {
        return this.mathService.findSquare(input)
                .defaultIfEmpty(new Response(-1));
    }

    // sends the data as a whole. it "collects" all data first and then returns
    @GetMapping("table/{input}")
    public Flux<Response> multiplicationTable(@PathVariable int input) {
        return this.mathService.multiplicationTable(input);
        // IMP: Backend won't process further if the subscriber (client here) cancels the request unlike non-reactive
    }

    // sends data as and when the data is ready, as a stream
    @GetMapping(value = "table/{input}/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Response> multiplicationTableStream(@PathVariable int input) {
        return this.mathService.multiplicationTable(input);
        // IMP: Backend won't process further if the subscriber (client here) cancels the request unlike non-reactive
    }

    @PostMapping("multiply")
    public Mono<Response> multiply(@RequestBody Mono<MultiplyRequestDto> requestDtoMono,
                                   @RequestHeader Map<String, String> headers) {
        // Support for mono in request body - springboot reads request in non-blocking
        // Header access remains same
        System.out.println(headers);
        return this.mathService.multiply(requestDtoMono);
    }


}
