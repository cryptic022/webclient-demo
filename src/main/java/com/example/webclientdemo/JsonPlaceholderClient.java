package com.example.webclientdemo;

import com.example.webclientdemo.model.Post;
import com.example.webclientdemo.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;


@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class JsonPlaceholderClient {

    private final WebClient rawWebClient;

    public Flux<Post> getPosts() {
        return rawWebClient
                .get().uri("/posts")
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, response -> {
                    System.out.println("4xx error");
                    return Mono.error(new RuntimeException("4xx"));
                })
                .onStatus(HttpStatus::is5xxServerError, response -> {
                    System.out.println("5xx error");
                    return Mono.error(new RuntimeException("5xx"));
                })
                .bodyToFlux(Post.class);
    }

    public Flux<Post> getPost(int userId) {
        return rawWebClient
                .get().uri("/posts?userId=" + userId)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, response -> {
                    System.out.println("4xx error");
                    return Mono.error(new RuntimeException("4xx"));
                })
                .onStatus(HttpStatus::is5xxServerError, response -> {
                    System.out.println("5xx error");
                    return Mono.error(new RuntimeException("5xx"));
                })
                .bodyToFlux(Post.class);
    }


    public Mono<List<Post>> getSequenceCallExample() {
        return rawWebClient.get()
                .uri("/users/" + getRandomNumber())
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, response -> {
                    System.out.println("4xx error");
                    return Mono.error(new RuntimeException("4xx"));
                })
                .onStatus(HttpStatus::is5xxServerError, response -> {
                    System.out.println("5xx error");
                    return Mono.error(new RuntimeException("5xx"));
                })
                .bodyToMono(User.class)
                .zipWhen(this::getPost,
                        (User, Post) -> Post

                );
    }

    public Mono<List<Post>> getPost(User user) {
        return rawWebClient
                .get().uri("/posts?userId=" + user.getId())
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, response -> {
                    System.out.println("4xx error");
                    return Mono.error(new RuntimeException("4xx"));
                })
                .onStatus(HttpStatus::is5xxServerError, response -> {
                    System.out.println("5xx error");
                    return Mono.error(new RuntimeException("5xx"));
                })
                .bodyToFlux(Post.class)
                .collectList();
    }


    public int getRandomNumber() {
        int min = 1;
        int max = 10;
        return (int)(Math.random() * (max - min + 1) + min);
    }
}
