package com.atrilos.socksrest.repository;

import com.atrilos.socksrest.model.Socks;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class SocksRepositoryTest {

    @Container
    public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>(DockerImageName.parse("postgres:alpine"));
    @Autowired
    private SocksRepository out;

    @Autowired
    private TestEntityManager entityManager;

    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url=", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username=", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password=", postgreSQLContainer::getPassword);
    }

    @Test
    public void findByColorAndCottonPart() {
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

        entityManager.persist(socksBlack);
        entityManager.persist(socksWhite);

        assertThat(out.findByColorAndCottonPart("black", 30))
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(Optional.of(socksBlack));
        assertThat(out.findByColorAndCottonPart("white", 60))
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(Optional.of(socksWhite));
    }
}