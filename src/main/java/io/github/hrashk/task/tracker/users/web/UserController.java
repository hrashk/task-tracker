package io.github.hrashk.task.tracker.users.web;

import io.github.hrashk.task.tracker.users.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserMapper mapper;

    @GetMapping
    public Flux<UserModel> getAllUsers() {
        return userService.getAllUsers()
                .map(mapper::map);
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<UserModel>> getById(@PathVariable String id) {
        return userService.findById(id)
                .map(mapper::map)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build())
        ;
    }

    @PostMapping
    public Mono<ResponseEntity<UserModel>> createUser(@RequestBody UserModel user) {
        return userService.save(mapper.map(user))
                .map(mapper::map)
                .map(body -> ResponseEntity
                        .created(URI.create("/api/v1/users/" + body.id()))
                        .body(body));
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<UserModel>> updateUser(@PathVariable String id, @RequestBody UserModel user) {
        return userService.update(id, mapper.map(user))
                .map(mapper::map)
                .map(ResponseEntity::ok);
    }
}
