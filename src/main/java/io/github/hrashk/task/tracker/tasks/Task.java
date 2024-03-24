package io.github.hrashk.task.tracker.tasks;

import io.github.hrashk.task.tracker.users.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Document
public class Task {
    @Id
    private String id;
    private String name;
    private String description;
    private Instant createdAt;
    private Instant updatedAt;
    private TaskStatus status;
    private String authorId;
    private String assigneeId;
    private Set<String> observerIds;

    @ReadOnlyProperty
    User author;
    @ReadOnlyProperty
    User assignee;
    @ReadOnlyProperty
    Set<User> observers;

    public Collection<String> mergeUserIds() {
        Set<String> ids = new HashSet<>();

        if (getObserverIds() != null) {
            getObserverIds().stream()
                    .filter(Objects::nonNull)
                    .forEach(ids::add);
        }

        if (getAuthorId() != null) {
            ids.add(getAuthorId());
        }

        if (getAssigneeId() != null) {
            ids.add(getAssigneeId());
        }

        return ids;
    }

    public void updateUsers(Map<String, User> userMap) {
        if (getAuthorId() != null) {
            setAuthor(userMap.get(getAuthorId()));
        }

        if (getAssigneeId() != null) {
            setAssignee(userMap.get(getAssigneeId()));
        }

        if (getObserverIds() != null) {
            Set<User> observers = getObserverIds().stream()
                    .filter(Objects::nonNull)
                    .map(userMap::get)
                    .collect(Collectors.toSet());
            setObservers(observers);
        }
    }

    public void addObserverId(String observerId) {
        if (observerIds == null) {
            observerIds = new HashSet<>();
        }

        observerIds.add(observerId);
    }

    public static class TaskBuilder {
        public TaskBuilder author(User user) {
            this.author = user;

            if (user != null) {
                this.authorId = user.getId();
            }

            return this;
        }

        public TaskBuilder assignee(User user) {
            this.assignee = user;

            if (user != null) {
                this.assigneeId = user.getId();
            }

            return this;
        }

        public TaskBuilder observers(Set<User> users) {
            this.observers = users;

            if (users != null) {
                this.observerIds = users.stream().map(User::getId).collect(Collectors.toSet());
            }

            return this;
        }
    }
}
