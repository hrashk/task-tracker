package io.github.hrashk.task.tracker.tasks.web;

import io.github.hrashk.task.tracker.tasks.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;

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

    @GetMapping("/{id}")
    public Mono<ResponseEntity<TaskModel>> getById(@PathVariable String id) {
        return taskService.findById(id)
                .map(mapper::map)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Mono<ResponseEntity<TaskModel>> createUser(@RequestBody TaskModel task) {
        return taskService.save(mapper.map(task))
                .map(mapper::map)
                .map(body -> ResponseEntity
                        .created(URI.create("/api/v1/tasks" + body.id()))
                        .body(body));
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<TaskModel>> updateTask(@PathVariable String id, @RequestBody TaskModel task) {
        return taskService.update(id, mapper.map(task))
                .map(mapper::map)
                .map(ResponseEntity::ok);
    }

    @PostMapping("/{id}/observers")
    public Mono<ResponseEntity<TaskModel>> addObserver(@PathVariable String id, @RequestBody ObserverModel observer) {
        return taskService.addObserver(id, observer.id())
                .map(mapper::map)
                .map(ResponseEntity::ok);
    }
}
