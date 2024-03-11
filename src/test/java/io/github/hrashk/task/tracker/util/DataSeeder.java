package io.github.hrashk.task.tracker.util;

import io.github.hrashk.task.tracker.users.User;
import io.github.hrashk.task.tracker.users.UserRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import net.datafaker.Faker;
import org.springframework.boot.test.context.TestComponent;

import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

@TestComponent
@RequiredArgsConstructor
@Accessors(fluent = true)
public class DataSeeder {
    private final UserRepository userRepository;
    private final Random random = ThreadLocalRandom.current();
    private final Faker faker = new Faker(random);

    @Getter
    private List<User> users;

    public void seed(int count) {
        users = userRepository.saveAll(sampleUsers(count)).collectList().block();
    }

    private Iterable<User> sampleUsers(int count) {
        return IntStream.range(0, count).mapToObj(idx -> aRandomUser()).toList();
    }

    private User aRandomUser() {
        String username = faker.internet().username();

        return new User().toBuilder()
                .id(UUID.randomUUID().toString())
                .username(username)
                .email(faker.internet().emailAddress(username))
                .build();
    }

    public void wipe() {
        userRepository.deleteAll().block();
    }
}
