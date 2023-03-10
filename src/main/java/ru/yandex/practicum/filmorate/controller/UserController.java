package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.controller.exception.RecordNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.Collection;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final HashMap<Integer, User> users = new HashMap<>();
    private static final AtomicInteger id = new AtomicInteger(0);

    @GetMapping()
    public Collection<User> findAll() {
        return users.values();
    }

    @PostMapping()
    public User create(@Valid @RequestBody User user) {
        user.setId(id.incrementAndGet());
        if (Objects.isNull(user.getName())) {
            user.setName(user.getLogin());
        }
        users.put(user.getId(), user);

        log.debug("New user was created with data {}.", user);

        return user;
    }

    @PutMapping()
    public ResponseEntity<User> update(@Valid @RequestBody User user) {
        if (!users.containsKey(user.getId())) {
            throw new RecordNotFoundException("Can not find user");
        }

        if (Objects.isNull(user.getName())) {
            user.setName(user.getLogin());
            log.info("User with id {} has no name. Login was set.", user.getId());
        }
        users.put(user.getId(), user);
        log.debug("User with id {} was changed with data {}.", user.getId(), user);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}

