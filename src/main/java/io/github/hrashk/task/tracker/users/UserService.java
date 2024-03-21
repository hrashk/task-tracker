package io.github.hrashk.task.tracker.users;

import io.github.hrashk.task.tracker.util.BeanCopyUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository repository;

    public Flux<User> getAllUsers() {
        return repository.findAll();
    }

    public Flux<User> getUsersById(Iterable<String> ids) {
        return repository.findAllById(ids);
    }

    public Mono<User> findById(String id) {
        return repository.findById(id);
    }

    public Mono<User> save(User user) {
        return repository.save(user);
    }

    public Mono<User> update(String id, User user) {
        return findById(id).flatMap(updatedUser -> {
            BeanCopyUtils.copyProperties(user, updatedUser);

            return repository.save(updatedUser);
        });
    }

    public Mono<Void> deleteById(String id) {
        return repository.deleteById(id);
    }
}
