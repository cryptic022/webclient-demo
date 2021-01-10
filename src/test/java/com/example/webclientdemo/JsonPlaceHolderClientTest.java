package com.example.webclientdemo;

import com.example.webclientdemo.model.Post;
import com.example.webclientdemo.model.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.models.HttpMethod;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockserver.junit.jupiter.MockServerExtension;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;
import org.mockserver.model.MediaType;
import org.mockserver.verify.VerificationTimes;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;


@ExtendWith(MockServerExtension.class)
public class JsonPlaceHolderClientTest {

    private JsonPlaceholderClient jsonPlaceholderClient;
    private WebClient rawWebClient;
    private ClientAndServer clientAndServer;

    private static final ObjectMapper serializer = new ObjectMapper();

    @BeforeEach
    public void setup() {
        clientAndServer = ClientAndServer.startClientAndServer(2001);
        rawWebClient = WebClient.builder()
                .baseUrl("http://localhost:" + clientAndServer.getLocalPort())
                .build();
        jsonPlaceholderClient = new JsonPlaceholderClient(rawWebClient);

    }

    @AfterEach
    public void tearDownServer() {
        clientAndServer.stop();
    }

    @Test
    public void testGetPostWithUserId() throws JsonProcessingException{

        String responseBody = getPostsAsString();

        HttpRequest expectedFirstRequest = HttpRequest.request()
                .withMethod(HttpMethod.GET.name())
                .withPath("/posts");

        clientAndServer.when(
                expectedFirstRequest
        ).respond(
                HttpResponse.response()
                        .withBody(responseBody)
                        .withContentType(MediaType.APPLICATION_JSON)
        );

        List<Post> post = jsonPlaceholderClient.getPosts().collectList().block();

        assertEquals(post.get(0).getId(), 1);
        assertEquals(post.get(0).getTitle(), "hello");

        clientAndServer.verify(
                HttpRequest.request().withMethod(HttpMethod.GET.name())
                        .withPath("/posts")
        );
    }

    @Test
    public void testGetPosts() throws JsonProcessingException {
        String responseBody = getPostsAsString();

        HttpRequest expectedFirstRequest = HttpRequest.request()
                .withMethod(HttpMethod.GET.name())
                .withPath("/posts");

        clientAndServer.when(
                expectedFirstRequest
        ).respond(
                HttpResponse.response()
                        .withBody(responseBody)
                        .withContentType(MediaType.APPLICATION_JSON)
        );

        List<Post> post = jsonPlaceholderClient.getPost(1).collectList().block();

        assertEquals(post.get(0).getId(), 1);
        assertEquals(post.get(0).getTitle(), "hello");

        clientAndServer.verify(
                HttpRequest.request().withMethod(HttpMethod.GET.name())
                        .withPath("/posts")
        );
    }

    @Test
    public void testGetPostsByUser() throws JsonProcessingException {

        User user = new User();
        user.setId(1);
        user.setEmail("hello@gmail.com");
        user.setUsername("panakj");


        String responseBody = getPostsAsString();

        HttpRequest expectedFirstRequest = HttpRequest.request()
                .withMethod(HttpMethod.GET.name())
                .withPath("/posts");

        clientAndServer.when(
                expectedFirstRequest
        ).respond(
                HttpResponse.response()
                        .withBody(responseBody)
                        .withContentType(MediaType.APPLICATION_JSON)
        );

        List<Post> post = jsonPlaceholderClient.getPost(user).block();

        assertEquals(post.get(0).getId(), 1);
        assertEquals(post.get(0).getTitle(), "hello");

        clientAndServer.verify(
                HttpRequest.request().withMethod(HttpMethod.GET.name())
                        .withPath("/posts")
        );
    }

    @Test
    public void testGetSequenceCallExample() throws JsonProcessingException {

        JsonPlaceholderClient jsonPlaceholderClientSpy = Mockito.spy(jsonPlaceholderClient);

        Mockito.when(jsonPlaceholderClientSpy.getRandomNumber()).thenReturn(1);

        HttpRequest expectedFirstRequest = HttpRequest.request()
                .withMethod(HttpMethod.GET.name())
                .withPath("/users/1");

        clientAndServer.when(
                expectedFirstRequest
        ).respond(
                HttpResponse.response()
                        .withBody("{\n" +
                                "    \"id\": 1,\n" +
                                "    \"name\": \"Leanne Graham\",\n" +
                                "    \"username\": \"Bret\",\n" +
                                "    \"email\": \"Sincere@april.biz\"}")
                        .withContentType(MediaType.APPLICATION_JSON)
        );

        User user = new User();
        user.setId(1);
        user.setEmail("hello@gmail.com");
        user.setUsername("panakj");


        String responseBody = getPostsAsString();

        HttpRequest expectedSecondRequest = HttpRequest.request()
                .withMethod(HttpMethod.GET.name())
                .withPath("/posts");

        clientAndServer.when(
                expectedSecondRequest
        ).respond(
                HttpResponse.response()
                        .withBody(responseBody)
                        .withContentType(MediaType.APPLICATION_JSON)
        );

        List<Post> post = jsonPlaceholderClientSpy.getSequenceCallExample().block();

        assertEquals(post.get(0).getId(), 1);
        assertEquals(post.get(0).getTitle(), "hello");

        clientAndServer.verify(
                HttpRequest.request().withMethod(HttpMethod.GET.name())
                        .withPath("/posts")
        );

    }


    private String getPostsAsString() throws JsonProcessingException {
        Post post = new Post();

        post.setId(1);
        post.setUserId(1);
        post.setTitle("hello");
        post.setBody("body");

        return serializer.writeValueAsString(Arrays.asList(post));
    }
}
