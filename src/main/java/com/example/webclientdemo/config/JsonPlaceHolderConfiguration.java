package com.example.webclientdemo.config;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeFilterFunctions;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;


@Slf4j
@Component
public class JsonPlaceHolderConfiguration {
    private static final String BASE_URL = "https://jsonplaceholder.typicode.com";

//    @Bean
//    public WebClient getRawWebClient(final WebClient.Builder webClientBuilder) {
//        return webClientBuilder
//                .baseUrl(BASE_URL)
//                .clientConnector(new ReactorClientHttpConnector(httpClient))
//                .defaultCookie("cookieKey", "cookieValue", "web", "client")
//                .defaultCookie("secretToken", UUID.randomUUID().toString())
//                .defaultHeader(HttpHeaders
//                        .CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
//                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
//                .defaultHeader(HttpHeaders.USER_AGENT, "webclient agent")
//                .filter(ExchangeFilterFunctions.basicAuthentication("pankaj", UUID.randomUUID().toString()))
//                .filter(logRequest())
//                .build();
//    }

    @Bean
    public WebClient rawWebClient() {

        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000)
                .doOnConnected(connection ->
                        connection.addHandlerLast(new ReadTimeoutHandler(2))
                                .addHandlerLast(new WriteTimeoutHandler(2)));

        return WebClient.builder()
                .baseUrl(BASE_URL)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .defaultCookie("cookieKey", "cookieValue", "web", "client")
                .defaultCookie("secretToken", UUID.randomUUID().toString())
                .defaultHeader(HttpHeaders
                        .CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.USER_AGENT, "webclient agent")
                .filter(ExchangeFilterFunctions.basicAuthentication("pankaj", UUID.randomUUID().toString()))
                .filter(logRequest())
                .build();
    }


    private ExchangeFilterFunction logRequest() {
        return (clientRequest, next) -> {
            log.info("Request: {} {}", clientRequest.method(), clientRequest.url());
            clientRequest.headers()
                    .forEach((name, values) -> values.forEach(value -> log.info("{}={}", name, value)));
            return next.exchange(clientRequest);
        };
    }
}
