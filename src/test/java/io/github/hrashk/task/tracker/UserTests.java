package io.github.hrashk.task.tracker;

import io.github.hrashk.task.tracker.users.web.UserModel;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;

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
        checkUserById(seeder.users().get(0).getId());
    }

    private void checkUserById(String userId) {
        webTestClient.get().uri("/api/v1/users/" + userId)
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserModel.class)
                .value(u -> assertThat(u.email()).isNotBlank())
        ;
    }

    @Test
    void createUser() {
        var user = new UserModel(null, "lorem", "lorem@ipsum.com");

        UserModel createdUser = webTestClient.post().uri("/api/v1/users")
                .body(Mono.just(user), UserModel.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(UserModel.class)
                .returnResult()
                .getResponseBody();

        assertThat(createdUser.id()).isNotBlank();

        checkUserById(createdUser.id());
    }
}
