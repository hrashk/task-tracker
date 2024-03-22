package io.github.hrashk.task.tracker.tasks;

import io.github.hrashk.task.tracker.users.User;
import io.github.hrashk.task.tracker.users.UserService;
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
                .flatMap(t -> userService.getUsersById(t.mergeUserIds())
                        .collectMap(User::getId)
                        .doOnNext(t::updateUsers)
                        .defaultIfEmpty(Map.of())
                        .thenReturn(t));
    }

    public Mono<Task> findById(String id) {
        return taskRepository.findById(id);
    }

    public Mono<Task> save(Task task) {
        return taskRepository.save(task);
    }
}
