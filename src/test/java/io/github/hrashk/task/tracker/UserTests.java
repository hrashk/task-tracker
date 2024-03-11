package io.github.hrashk.task.tracker;

import io.github.hrashk.task.tracker.users.web.UserModel;
import org.junit.jupiter.api.Test;

class UserTests extends IntegrationTest {

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
