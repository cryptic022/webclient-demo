package com.example.webclientdemo;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Slf4j
@RestController
@RequestMapping("/api")
public class JsonPlaceHolderController {

    @Autowired
    private jsonPlaceholderClient jsonPlaceholderClient;


    @GetMapping("/todos")
    public Flux<JsonNode> getTodos() {
        return jsonPlaceholderClient.getTodos();
    }

    @GetMapping("/todos/{todoId}")
    public Mono<JsonNode> getTodoById(@PathVariable String todoId) {
        return jsonPlaceholderClient.getTodo(todoId);
    }
}
