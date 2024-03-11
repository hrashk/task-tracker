package io.github.hrashk.task.tracker;

import io.github.hrashk.task.tracker.users.web.UserModel;
import io.github.hrashk.task.tracker.util.MongodbInitializer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest
@AutoConfigureWebTestClient
@ContextConfiguration(initializers = MongodbInitializer.class)
class UserIntegrationTests {
    @Autowired
    protected WebTestClient webTestClient;

    @Test
    void getAllUsers() {
        webTestClient.get().uri("/api/v1/users")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(UserModel.class)
                .hasSize(5)
        ;
    }
}
