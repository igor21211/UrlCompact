package com.example.urlcompact.IntegrationTest;

import com.example.urlcompact.dto.UrlDto;
import com.example.urlcompact.model.Url;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.assertj.core.api.Assertions.*;

import static org.bson.assertions.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureWebTestClient
public class UrlIntegrationsTest {

    @Autowired
    private WebTestClient webTestClient;

    private static final String FULL_URL = "https://example.com";
    private static final String COMPACT_URL = "http://edp/oxl";


    @Test
    void canGenerateUrl() {
        Url url = new Url();
        url.setId(RandomStringUtils.randomAlphabetic(6).toLowerCase());
        url.setFullUrl(FULL_URL);
        url.setCompactUrl(COMPACT_URL);

        webTestClient.post()
                .uri("api/v1/generate")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(url), Url.class)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(String.class)
                .value(responseBody -> {
                    assertThat(responseBody).contains("fullUrl");
                    assertThat(responseBody).contains("compactUrl");
                    jsonPath("$.fullUrl").value(FULL_URL);
                    jsonPath("$.compactUrl").value(COMPACT_URL);
                });
    }

    @Test
    void failInvalidUrl(){
        Url url = new Url();
        url.setId(RandomStringUtils.randomAlphabetic(6).toLowerCase());
        url.setFullUrl("dsfsdf");

        webTestClient.post()
                .uri("api/v1/generate")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(url), Url.class)
                .exchange()
                .expectStatus()
                .isBadRequest()
                .expectBody(String.class)
                .value(responseBody -> {
                    jsonPath("$.message").value("Invalid Url");
                });
    }

    @Test
    void canGetByCompactUrl(){
        Url url = new Url();
        url.setCompactUrl(COMPACT_URL);

        webTestClient.post()
                .uri("api/v1/regenerate")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(url), Url.class)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(String.class)
                .value(responseBody -> {
                    assertThat(responseBody).contains("fullUrl");
                    assertThat(responseBody).contains("compactUrl");
                    jsonPath("$.fullUrl").value(FULL_URL);
                    jsonPath("$.compactUrl").value(COMPACT_URL);
                });
    }

    @Test
    void cannotFindCompactUrl(){
        Url url = new Url();
        String wrongUrl = "http://ojh/iuzs";
        url.setCompactUrl(wrongUrl);

        webTestClient.post()
                .uri("api/v1/regenerate")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(url), Url.class)
                .exchange()
                .expectStatus()
                .isNotFound()
                .expectBody(String.class)
                .value(responseBody -> {
                    jsonPath("$.message").value("Something wrong cannot find url : "+ wrongUrl);
                });
    }

    @Test
    void regenerateCompactUrl(){
        Url url = new Url();
        url.setId(RandomStringUtils.randomAlphabetic(6).toLowerCase());
        url.setFullUrl(FULL_URL);
        url.setCompactUrl(COMPACT_URL);

        webTestClient.post()
                .uri("api/v1/regenerateCompact")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(url), Url.class)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(String.class)
                .value(responseBody -> {
                    assertThat(responseBody).contains("fullUrl");
                    assertThat(responseBody).contains("compactUrl");
                    jsonPath("$.fullUrl").value(FULL_URL);
                    jsonPath("$.compactUrl").value(COMPACT_URL);
                });
    }

    @Test
    void failInvalidUrlWhenRegenerateCompactUrl(){
        Url url = new Url();
        url.setId(RandomStringUtils.randomAlphabetic(6).toLowerCase());
        url.setFullUrl("dsfsdf");

        webTestClient.post()
                .uri("api/v1/regenerateCompact")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(url), Url.class)
                .exchange()
                .expectStatus()
                .isBadRequest()
                .expectBody(String.class)
                .value(responseBody -> {
                    jsonPath("$.message").value("Invalid Url");
                });
    }
}
