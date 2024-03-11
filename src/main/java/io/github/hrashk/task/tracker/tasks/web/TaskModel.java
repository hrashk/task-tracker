package io.github.hrashk.task.tracker.tasks.web;

import io.github.hrashk.task.tracker.tasks.TaskStatus;

import java.time.Instant;

public record TaskModel(
        String id,
        String name,
        String description,
        Instant createdAt,
        Instant updatedAt,
        TaskStatus status
) {
}
