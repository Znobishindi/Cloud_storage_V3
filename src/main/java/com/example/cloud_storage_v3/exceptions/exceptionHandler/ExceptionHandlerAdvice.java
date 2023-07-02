package com.example.cloud_storage_v3.exceptions.exceptionHandler;

import com.example.cloud_storage_v3.exceptions.FileNotFoundException;
import com.example.cloud_storage_v3.exceptions.InvalidInputData;
import com.example.cloud_storage_v3.exceptions.UserNotFoundException;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class ExceptionHandlerAdvice {
    @ExceptionHandler(InvalidInputData.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExResponse invalidInputDataHandler(InvalidInputData exc) {
        log.error("message: {}, id: = {}", exc.getMessage(), exc.getId());
        return new ExResponse(exc.getMessage(), exc.getId());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExResponse handleValidationErrors(@NonNull final MethodArgumentNotValidException exc) {
        String msg = exc.getBindingResult().getFieldErrors()
                .stream().map(FieldError::getDefaultMessage).collect(Collectors.joining(" | "));
        log.error("message: {}, id: = {}", msg, 0);
        return new ExResponse(msg, 0);
    }

    @ExceptionHandler(FileNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExResponse fileNotFoundHandler(@NonNull final FileNotFoundException exc) {
        log.error("message: {}, id: = {}", exc.getMessage(), exc.getId());
        return new ExResponse(exc.getMessage(), exc.getId());
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExResponse userNotFoundHandler(@NonNull final UserNotFoundException exc) {
        log.error("message: {}, id: = {}", exc.getMessage(), exc.getId());
        return new ExResponse(exc.getMessage(), exc.getId());
    }

    @ExceptionHandler()
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ExResponse errorHandler(@NonNull final Exception exc) {
        log.error("message: {}, id: = {}", exc.getMessage(), 0);
        return new ExResponse(exc.getMessage(), 0);
    }
}
