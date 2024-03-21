package io.github.hrashk.task.tracker;

import io.github.hrashk.task.tracker.users.User;
import io.github.hrashk.task.tracker.users.web.UserModel;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;

import static org.assertj.core.api.Assertions.assertThat;

class UserTests extends IntegrationTest {
    private static final String FANCY_EMAIL = "lorem@ipsum.com";

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
        User u = seeder.users().get(0);
        checkUserById(u.getId(), u.getEmail());
    }

    private void checkUserById(String userId, String email) {
        webTestClient.get().uri("/api/v1/users/" + userId)
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserModel.class)
                .value(u -> assertThat(u.email()).isEqualTo(email))
        ;
    }

    @Test
    void createUser() {
        var user = new UserModel(null, "lorem", FANCY_EMAIL);

        UserModel createdUser = webTestClient.post().uri("/api/v1/users")
                .body(Mono.just(user), UserModel.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(UserModel.class)
                .returnResult()
                .getResponseBody();

        assertThat(createdUser.id()).isNotBlank();

        checkUserById(createdUser.id(), FANCY_EMAIL);
    }

    @Test
    void updateUser() {
        User user = seeder.users().get(0);
        UserModel userModel = new UserModel(user.getId(), user.getUsername(), FANCY_EMAIL);

        UserModel updatedUser = webTestClient.put().uri("/api/v1/users/" + userModel.id())
                .body(Mono.just(userModel), UserModel.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserModel.class)
                .returnResult()
                .getResponseBody();

        assertThat(updatedUser.id()).isEqualTo(userModel.id());

        checkUserById(updatedUser.id(), FANCY_EMAIL);
    }
}
