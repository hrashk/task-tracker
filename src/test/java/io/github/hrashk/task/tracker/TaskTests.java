package io.github.hrashk.task.tracker;

import io.github.hrashk.task.tracker.tasks.web.TaskModel;
import org.junit.jupiter.api.Test;

public class TaskTests extends IntegrationTest {

    @Test
    void getAllTasks() {
        webTestClient.get().uri("/api/v1/tasks")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(TaskModel.class)
                .hasSize(5)
        ;
    }
}
