package nl.casperdaris.boekenkast.handler;

import java.util.HashSet;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.mail.MessagingException;

/// Dit is de globale exception handler die alle exceptions afhandelt in de API.
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(LockedException.class)
    public ResponseEntity<ExceptionResponse> handleException(LockedException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ExceptionResponse
                        .builder()
                        .errorCode(ExceptionStatusCodes.ACCOUNT_DISABLED.getCode())
                        .errorDescription(ExceptionStatusCodes.ACCOUNT_DISABLED.getDescription())
                        .error(e.getMessage())
                        .build());
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<ExceptionResponse> handleException(DisabledException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ExceptionResponse
                        .builder()
                        .errorCode(ExceptionStatusCodes.ACCOUNT_DISABLED.getCode())
                        .errorDescription(ExceptionStatusCodes.ACCOUNT_DISABLED.getDescription())
                        .error(e.getMessage())
                        .build());
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ExceptionResponse> handleException(BadCredentialsException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ExceptionResponse
                        .builder()
                        .errorCode(ExceptionStatusCodes.INCORRECT_CREDENTIALS.getCode())
                        .errorDescription(ExceptionStatusCodes.INCORRECT_CREDENTIALS.getDescription())
                        .error(e.getMessage())
                        .build());
    }

    @ExceptionHandler(MessagingException.class)
    public ResponseEntity<ExceptionResponse> handleException(MessagingException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ExceptionResponse
                        .builder()
                        .error(e.getMessage())
                        .build());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleException(MethodArgumentNotValidException e) {
        Set<String> errors = new HashSet<>();
        e.getBindingResult().getAllErrors().forEach(error -> errors.add(error.getDefaultMessage()));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ExceptionResponse
                        .builder()
                        .validationErrors(errors)
                        .build());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleException(Exception e) {
        // TODO: Log the exception
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ExceptionResponse
                        .builder()
                        .errorDescription("Internal server error")
                        .error(e.getMessage())
                        .build());
    }
}
