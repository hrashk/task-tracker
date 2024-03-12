package io.github.hrashk.task.tracker.users.web;

import io.github.hrashk.task.tracker.users.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

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
}
