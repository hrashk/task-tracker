package io.github.hrashk.task.tracker;

import io.github.hrashk.task.tracker.tasks.Task;
import io.github.hrashk.task.tracker.tasks.TaskStatus;
import io.github.hrashk.task.tracker.tasks.web.TaskModel;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class TaskTests extends IntegrationTest {

    private static final String FANCY_NAME = "Fancy task";

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
        Task t = seeder.tasks().get(0);

        checkTaskName(t.getId(), t.getName());
    }

    private void checkTaskName(String id, String name) {
        webTestClient.get().uri("/api/v1/tasks/" + id)
                .exchange()
                .expectStatus().isOk()
                .expectBody(TaskModel.class)
                .value(tm -> assertThat(tm.name()).isEqualTo(name));
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
    void updateTask() {}

    @Test
    void updateAuthor() {}

    @Test
    void updateAssignee() {}

    @Test
    void addObserver() {}

    @Test
    void deleteById() {}
}
