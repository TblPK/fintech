package com.weather;

import com.weather.exception.WeatherNotFoundException;
import com.weather.services.TransactionalJdbcService;
import com.weather.services.TransactionalJpaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@Testcontainers
public class TestContainersH2Test {

    @Container
    public static GenericContainer h2 = new GenericContainer(DockerImageName.parse("oscarfonts/h2"))
            .withExposedPorts(1521, 81)
            .withEnv("H2_OPTIONS", "-ifNotExists")
            .waitingFor(Wait.defaultWaitStrategy());

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", () -> "jdbc:h2:tcp://localhost:" + h2.getMappedPort(1521) + "/test");
        registry.add("spring.datasource.username", () -> "sa");
        registry.add("spring.datasource.password", () -> "");
    }

    @Autowired
    TransactionalJpaService transactionalJpaService;

    @Autowired
    TransactionalJdbcService transactionalJdbcService;

    @Test
    public void updateWeathersByNameWithInvalidNameReturnWeatherNotFoundException() {
        assertThrows(WeatherNotFoundException.class, () -> transactionalJpaService.updateWeathersByName("Mo"));
        assertThrows(WeatherNotFoundException.class, () -> transactionalJdbcService.updateWeathersByName("Mo"));
    }

}