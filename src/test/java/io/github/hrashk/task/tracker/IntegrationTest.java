package io.github.hrashk.task.tracker;

import io.github.hrashk.task.tracker.util.DataSeeder;
import io.github.hrashk.task.tracker.util.MongodbInitializer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest
@AutoConfigureWebTestClient
@ContextConfiguration(initializers = MongodbInitializer.class)
@Import(DataSeeder.class)
public abstract class IntegrationTest {
    @Autowired
    protected WebTestClient webTestClient;

    @Autowired
    protected DataSeeder seeder;

    @BeforeEach
    void seedData() {
        seeder.seed(5);
    }

    @AfterEach
    void wipeData() {
        seeder.wipe();
    }
}
