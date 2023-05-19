package com.atrilos.socksrest.controller;

import com.atrilos.socksrest.model.Socks;
import com.atrilos.socksrest.repository.SocksRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class SocksControllerTest {

    @Container
    public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>(DockerImageName.parse("postgres:alpine"));
    @Autowired
    private WebTestClient webTestClient;
    @Autowired
    private SocksRepository socksRepository;

    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url=", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username=", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password=", postgreSQLContainer::getPassword);
    }

    @BeforeEach
    public void setUp() {
        socksRepository.deleteAll();
    }

    @Test
    public void getSocks_ByColor() {
        Socks socksBlack = Socks.builder()
                .color("black")
                .cottonPart(30)
                .quantity(15L)
                .build();
        Socks socksWhite = Socks.builder()
                .color("white")
                .cottonPart(60)
                .quantity(19L)
                .build();

        socksRepository.save(socksBlack);
        socksRepository.save(socksWhite);

        Long responseWhite = webTestClient.get()
                .uri("/api/socks?color=white")
                .exchange()
                .expectStatus().isOk()
                .expectBody(Long.class)
                .returnResult().getResponseBody();
        Long responseBlack = webTestClient.get()
                .uri("/api/socks?color=black")
                .exchange()
                .expectStatus().isOk()
                .expectBody(Long.class)
                .returnResult().getResponseBody();

        assertThat(responseWhite)
                .isEqualTo(socksWhite.getQuantity());
        assertThat(responseBlack)
                .isEqualTo(socksBlack.getQuantity());
    }

    @Test
    public void getSocks_ByCottonPart() {
        Socks socksBlack = Socks.builder()
                .color("black")
                .cottonPart(30)
                .quantity(15L)
                .build();
        Socks socksWhite = Socks.builder()
                .color("white")
                .cottonPart(60)
                .quantity(19L)
                .build();

        socksRepository.save(socksBlack);
        socksRepository.save(socksWhite);

        Long responseBoth = webTestClient.get()
                .uri("/api/socks?operation=moreThan&cottonPart=29")
                .exchange()
                .expectStatus().isOk()
                .expectBody(Long.class)
                .returnResult().getResponseBody();
        Long responseWhite = webTestClient.get()
                .uri("/api/socks?operation=moreThan&cottonPart=30")
                .exchange()
                .expectStatus().isOk()
                .expectBody(Long.class)
                .returnResult().getResponseBody();
        Long responseBlackEqual = webTestClient.get()
                .uri("/api/socks?operation=equal&cottonPart=30")
                .exchange()
                .expectStatus().isOk()
                .expectBody(Long.class)
                .returnResult().getResponseBody();
        Long responseNoneEqual = webTestClient.get()
                .uri("/api/socks?operation=equal&cottonPart=29")
                .exchange()
                .expectStatus().isOk()
                .expectBody(Long.class)
                .returnResult().getResponseBody();
        Long responseBlackLT = webTestClient.get()
                .uri("/api/socks?operation=lessThan&cottonPart=31")
                .exchange()
                .expectStatus().isOk()
                .expectBody(Long.class)
                .returnResult().getResponseBody();

        assertThat(responseBoth)
                .isEqualTo(socksWhite.getQuantity() + socksBlack.getQuantity());
        assertThat(responseWhite)
                .isEqualTo(socksWhite.getQuantity());
        assertThat(responseBlackEqual)
                .isEqualTo(socksBlack.getQuantity());
        assertThat(responseNoneEqual)
                .isEqualTo(0L);
        assertThat(responseBlackLT)
                .isEqualTo(socksBlack.getQuantity());
    }

    @Test
    public void getSocks_ByFullFilter() {
        Socks socksBlack = Socks.builder()
                .color("black")
                .cottonPart(30)
                .quantity(15L)
                .build();
        Socks socksWhite = Socks.builder()
                .color("white")
                .cottonPart(30)
                .quantity(19L)
                .build();

        socksRepository.save(socksBlack);
        socksRepository.save(socksWhite);

        Long responseWhite = webTestClient.get()
                .uri("/api/socks?operation=moreThan&cottonPart=29&color=white")
                .exchange()
                .expectStatus().isOk()
                .expectBody(Long.class)
                .returnResult().getResponseBody();
        Long responseNone = webTestClient.get()
                .uri("/api/socks?color=black&operation=moreThan&cottonPart=31")
                .exchange()
                .expectStatus().isOk()
                .expectBody(Long.class)
                .returnResult().getResponseBody();
        Long responseBlackEqual = webTestClient.get()
                .uri("/api/socks?operation=equal&cottonPart=30&color=black")
                .exchange()
                .expectStatus().isOk()
                .expectBody(Long.class)
                .returnResult().getResponseBody();

        assertThat(responseWhite)
                .isEqualTo(socksWhite.getQuantity());
        assertThat(responseBlackEqual)
                .isEqualTo(socksBlack.getQuantity());
        assertThat(responseNone)
                .isEqualTo(0L);
    }
}