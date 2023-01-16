package ru.yandex.practicum.filmorate.controller.exception;

import lombok.Data;

import java.time.LocalDateTime;
@Data
public class ErrorResponse {
    private LocalDateTime timestamp;
    private String status;
    private String error;
    private String path;

    public ErrorResponse(LocalDateTime timestamp, String status, String error, String path) {
        super();
        this.timestamp = timestamp;
        this.status = status;
        this.error = error;
        this.path = path;
    }
}

