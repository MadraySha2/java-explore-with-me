package server.exeption;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public stat.server.exeption.ErrorResponse handleWrongState(final NotSupportedException e) {
        return new stat.server.exeption.ErrorResponse(e.getMessage(), e.getMessage());
    }
}