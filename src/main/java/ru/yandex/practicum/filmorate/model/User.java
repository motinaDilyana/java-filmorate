package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.lang.Nullable;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class User {
    private int id;
    @NotBlank
    private String login;
    @Nullable
    private String name;
    @Email
    private String email;
    @Past
    private final LocalDate birthday;
}
