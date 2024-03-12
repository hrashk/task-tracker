package io.github.hrashk.task.tracker.tasks;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@RequiredArgsConstructor
@Service
public class TaskService {
    private final TaskRepository taskRepository;

    public Flux<Task> getAllTasks() {
        return taskRepository.findAll();
    }
}
