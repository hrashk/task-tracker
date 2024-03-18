package io.github.hrashk.task.tracker;

import io.github.hrashk.task.tracker.users.web.UserModel;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserTests extends IntegrationTest {
    @Test
    void getAllUsers() {
        webTestClient.get().uri("/api/v1/users")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(UserModel.class)
                .hasSize(7)
        ;
    }

    @Test
    void getUserById() {
        webTestClient.get().uri("/api/v1/users/" + seeder.users().get(0).getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserModel.class)
                .value(u -> assertThat(u.email()).isNotBlank())
        ;
    }
}
