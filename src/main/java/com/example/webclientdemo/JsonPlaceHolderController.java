package com.example.webclientdemo;

import com.example.webclientdemo.model.Todo;
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
    public Flux<Todo> getTodos() {
        return jsonPlaceholderClient.getTodos();
    }

    @GetMapping("/todos/{todoId}")
    public Mono<Todo> getTodoById(@PathVariable int todoId) {
        return jsonPlaceholderClient.getTodo(todoId);
    }
}
