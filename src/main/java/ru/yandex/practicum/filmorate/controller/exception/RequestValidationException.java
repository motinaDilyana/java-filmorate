package ru.yandex.practicum.filmorate.controller.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@SuppressWarnings({"unchecked", "rawtypes"})
@ControllerAdvice
@Slf4j
public class RequestValidationException extends ResponseEntityExceptionHandler {
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ErrorResponse error = new ErrorResponse(LocalDateTime.now(), status.toString(), ex.getLocalizedMessage(), request.getContextPath());
        log.warn("Validation request error with message {},  request {}",
                ex.getMessage(),
                ex.getParameter());
        return new ResponseEntity(error, HttpStatus.BAD_REQUEST);
    }
}
