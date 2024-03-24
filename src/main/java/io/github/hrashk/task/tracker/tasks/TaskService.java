package io.github.hrashk.task.tracker.tasks;

import io.github.hrashk.task.tracker.users.User;
import io.github.hrashk.task.tracker.users.UserService;
import io.github.hrashk.task.tracker.util.BeanCopyUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

@RequiredArgsConstructor
@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final UserService userService;

    public Flux<Task> getAllTasks() {
        return taskRepository.findAll()
                .flatMap(this::fetchLinkedUsers);
    }

    private Mono<Task> fetchLinkedUsers(Task t) {
        return userService.getUsersById(t.mergeUserIds())
                .collectMap(User::getId)
                .doOnNext(t::updateUsers)
                .defaultIfEmpty(Map.of())
                .thenReturn(t);
    }

    public Mono<Task> findById(String id) {
        return taskRepository.findById(id)
                .flatMap(this::fetchLinkedUsers);
    }

    public Mono<Task> save(Task task) {
        return taskRepository.save(task)
                .flatMap(this::fetchLinkedUsers);
    }

    public Mono<Task> update(String id, Task task) {
        return taskRepository.findById(id)
                .flatMap(taskToUpdate -> {
                    BeanCopyUtils.copyProperties(task, taskToUpdate);

                    return taskRepository.save(taskToUpdate);
                })
                .flatMap(this::fetchLinkedUsers);
    }

    public Mono<Task> addObserver(String id, String observerId) {
        return taskRepository.findById(id)
                .flatMap(taskToUpdate -> {
                    taskToUpdate.addObserverId(observerId);

                    return taskRepository.save(taskToUpdate);
                })
                .flatMap(this::fetchLinkedUsers);
    }

    public Mono<Void> deleteById(String id) {
        return taskRepository.deleteById(id);
    }
}
