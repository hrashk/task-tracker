package io.github.hrashk.task.tracker;

import io.github.hrashk.task.tracker.tasks.Task;
import io.github.hrashk.task.tracker.tasks.TaskStatus;
import io.github.hrashk.task.tracker.tasks.web.ObserverModel;
import io.github.hrashk.task.tracker.tasks.web.TaskMapper;
import io.github.hrashk.task.tracker.tasks.web.TaskModel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class TaskTests extends IntegrationTest {

    private static final String FANCY_NAME = "Fancy task";

    @Autowired
    private TaskMapper mapper;

    @Test
    void getAllTasks() {
        List<TaskModel> response = webTestClient.get().uri("/api/v1/tasks")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(TaskModel.class)
                .hasSize(3)
                .returnResult()
                .getResponseBody();

        assertThat(response).allSatisfy(tm ->
                assertThat(tm.author()).as("task author").isNotNull());
        assertThat(response).anySatisfy(tm ->
                assertThat(tm.assignee()).as("task assignee").isNotNull());
        assertThat(response).anySatisfy(tm ->
                assertThat(tm.observers()).as("task observers").isNotEmpty());
    }

    @Test
    void getTaskById() {
        Task t = seeder.tasks().get(2);

        TaskModel taskModel = checkTaskName(t.getId(), t.getName());

        assertThat(taskModel.author()).as("Task author").isNotNull();
        assertThat(taskModel.assignee()).as("Task assignee").isNotNull();
        assertThat(taskModel.observers()).as("Task observers").isNotEmpty();
    }

    private TaskModel checkTaskName(String id, String name) {
        return webTestClient.get().uri("/api/v1/tasks/" + id)
                .exchange()
                .expectStatus().isOk()
                .expectBody(TaskModel.class)
                .value(tm -> assertThat(tm.name()).isEqualTo(name))
                .returnResult()
                .getResponseBody();
    }

    @Test
    void createTask() {
        TaskModel task = new TaskModel(
                null,
                FANCY_NAME,
                "Some test desc",
                Instant.now(),
                Instant.now(),
                TaskStatus.TODO,
                null, null, null
        );

        TaskModel createdTask = webTestClient.post().uri("/api/v1/tasks")
                .body(Mono.just(task), TaskModel.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(TaskModel.class)
                .returnResult()
                .getResponseBody();

        assertThat(createdTask.id()).isNotBlank();

        checkTaskName(createdTask.id(), FANCY_NAME);
    }

    @Test
    void updateTask() {
        Task t = seeder.tasks().get(0).toBuilder()
                .name(FANCY_NAME)
                .assignee(seeder.users().get(4))
                .observers(Set.of(seeder.users().get(5), seeder.users().get(6)))
                .build();

        webTestClient.put().uri("/api/v1/tasks/" + t.getId())
                .body(Mono.just(mapper.map(t)), TaskModel.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(TaskModel.class)
                .value(tm -> assertThat(tm.id()).isEqualTo(t.getId()));

        TaskModel updated = checkTaskName(t.getId(), FANCY_NAME);
        assertThat(updated.assignee().id()).isEqualTo(seeder.users().get(4).getId());
        assertThat(updated.observers()).hasSize(2);
    }

    @Test
    void addObserver() {
        Task t = seeder.tasks().get(0);
        String observerId = seeder.users().get(5).getId();

        webTestClient.post().uri("/api/v1/tasks/%s/observers".formatted(t.getId()))
                .body(Mono.just(new ObserverModel(observerId)), ObserverModel.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(TaskModel.class)
                .value(tm -> assertThat(tm.id()).isEqualTo(t.getId()));

        TaskModel updated = checkTaskName(t.getId(), t.getName());
        assertThat(updated.observers()).hasSize(1);
        assertThat(updated.observers()).anyMatch(o -> Objects.equals(o.id(), observerId));
    }

    @Test
    void deleteById() {}
}
