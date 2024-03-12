package io.github.hrashk.task.tracker.util;

import io.github.hrashk.task.tracker.tasks.Task;
import io.github.hrashk.task.tracker.tasks.TaskRepository;
import io.github.hrashk.task.tracker.tasks.TaskStatus;
import io.github.hrashk.task.tracker.users.User;
import io.github.hrashk.task.tracker.users.UserRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import net.datafaker.Faker;
import org.springframework.boot.test.context.TestComponent;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

@TestComponent
@RequiredArgsConstructor
@Accessors(fluent = true)
public class DataSeeder {
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;
    private final Random random = ThreadLocalRandom.current();
    private final Faker faker = new Faker(random);

    @Getter
    private List<User> users;
    @Getter
    private List<Task> tasks;

    public void seed() {
        users = userRepository.saveAll(sampleUsers()).collectList().block();
        tasks = taskRepository.saveAll(sampleTasks()).collectList().block();
    }

    public void wipe() {
        taskRepository.deleteAll().block();
        userRepository.deleteAll().block();
    }

    private Iterable<User> sampleUsers() {
        return IntStream.range(0, 7).mapToObj(idx -> aRandomUser()).toList();
    }

    private User aRandomUser() {
        String username = faker.internet().username();

        return new User().toBuilder()
                .id(UUID.randomUUID().toString())
                .username(username)
                .email(faker.internet().emailAddress(username))
                .build();
    }

    private Iterable<Task> sampleTasks() {
        return List.of(task1(), task2(), task3());
    }

    private Task task1() {
        return new Task().toBuilder()
                .id(UUID.randomUUID().toString())
                .name("Task name 1")
                .description("Task description 1")
                .createdAt(Instant.now().minus(3, ChronoUnit.DAYS))
                .updatedAt(Instant.now().minus(1, ChronoUnit.DAYS))
                .status(TaskStatus.TODO)

                .authorId(users.get(0).getId())
                .author(users.get(0))

                .build();
    }

    private Task task2() {
        return new Task().toBuilder()
                .id(UUID.randomUUID().toString())
                .name("Task name 2")
                .description("Task description 2")
                .createdAt(Instant.now().minus(7, ChronoUnit.DAYS))
                .updatedAt(Instant.now().minus(5, ChronoUnit.DAYS))
                .status(TaskStatus.IN_PROGRESS)

                .authorId(users.get(1).getId())
                .author(users.get(1))

                .assigneeId(users.get(2).getId())
                .assignee(users.get(2))

                .build();
    }

    private Task task3() {
        return new Task().toBuilder()
                .id(UUID.randomUUID().toString())
                .name("Task name 3")
                .description("Task description 3")
                .createdAt(Instant.now().minus(2, ChronoUnit.DAYS))
                .updatedAt(Instant.now())
                .status(TaskStatus.DONE)

                .authorId(users.get(0).getId())
                .author(users.get(0))

                .assigneeId(users.get(3).getId())
                .assignee(users.get(3))

                .observerIds(Set.of(users.get(4).getId(), users.get(5).getId()))
                .observers(Set.of(users.get(4), users.get(5)))

                .build();
    }
}
