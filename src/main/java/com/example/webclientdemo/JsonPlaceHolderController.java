package com.example.webclientdemo;

import com.example.webclientdemo.model.Post;
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


    @GetMapping("/posts")
    public Flux<Post> getPosts() {
        return jsonPlaceholderClient.getPosts();
    }

    @GetMapping("/posts/{userId}")
    public Flux<Post> getPostById(@PathVariable int userId) {
        return jsonPlaceholderClient.getPost(userId);
    }
}
