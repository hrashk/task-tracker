package io.github.hrashk.task.tracker;

import io.github.hrashk.task.tracker.tasks.web.TaskModel;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class TaskTests extends IntegrationTest {

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
}
