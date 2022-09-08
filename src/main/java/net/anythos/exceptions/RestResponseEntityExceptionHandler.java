package net.anythos.exceptions;

import com.auth0.jwt.exceptions.TokenExpiredException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<Object> handleTokenExpiredException(TokenExpiredException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ExceptionMessage(
                                LocalDateTime.now(),
                                HttpStatus.FORBIDDEN.toString(),
                                ex.getMessage()
                        )
                );
    }
}
