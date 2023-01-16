package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.controller.exception.RecordNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.util.Collection;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final HashMap<Integer, Film> films = new HashMap<>();
    private static final AtomicInteger id = new AtomicInteger(0);

    @GetMapping()
    public Collection<Film> findAll() {
        return films.values();
    }

    @PostMapping()
    public Film create(@Valid @RequestBody Film film) {
        film.setId(id.incrementAndGet());
        films.put(film.getId(), film);
        log.debug("New film was created with data {}.", film);

        return film;
    }

    @PutMapping()
    public ResponseEntity<Film> update(@Valid @RequestBody Film film) {
        if (!films.containsKey(film.getId())) {
            throw new RecordNotFoundException("Can not find film");
        }
        films.put(film.getId(), film);
        log.debug("Film with id {} was changed with data {}.", film.getId(), film);
        return new ResponseEntity<>(film, HttpStatus.OK);
    }
}
