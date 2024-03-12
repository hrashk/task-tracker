package io.github.hrashk.task.tracker.tasks.web;

import io.github.hrashk.task.tracker.tasks.TaskStatus;
import io.github.hrashk.task.tracker.users.User;

import java.time.Instant;
import java.util.Set;

public record TaskModel(
        String id,
        String name,
        String description,
        Instant createdAt,
        Instant updatedAt,
        TaskStatus status,
        User author,
        User assignee,
        Set<User> observers
) {
}
