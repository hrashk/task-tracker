package io.github.hrashk.task.tracker.tasks.web;

import io.github.hrashk.task.tracker.tasks.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;
    private final TaskMapper mapper;

    @GetMapping
    public Flux<TaskModel> getAllTasks() {
        return taskService.getAllTasks()
                .map(mapper::map);
    }
}
