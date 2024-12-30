package nl.casperdaris.boekenkast.handler;

import org.springframework.http.HttpStatus;

import lombok.Getter;

/// Deze enum bevat alle exception status codes die de API kan genereren.
public enum ExceptionStatusCodes {

    NO_CODE(0, HttpStatus.NOT_IMPLEMENTED, "No code"),
    INCORRECT_CREDENTIALS(300, HttpStatus.BAD_REQUEST, "Current credentials are incorrect"),
    INCORRECT_PASSWORD(301, HttpStatus.BAD_REQUEST, "Current password is incorrect"),
    NEW_PASSWORD_DOES_NOT_MATCH(302, HttpStatus.BAD_REQUEST, "New password does not match"),
    ACCOUNT_LOCKED(303, HttpStatus.FORBIDDEN, "User account is locked"),
    ACCOUNT_DISABLED(304, HttpStatus.FORBIDDEN, "User account is disabled");

    @Getter
    private final Integer code;
    @Getter
    private final String description;
    @Getter
    private final HttpStatus httpStatus;

    ExceptionStatusCodes(Integer code, HttpStatus httpStatus, String description) {
        this.code = code;
        this.description = description;
        this.httpStatus = httpStatus;
    }
}
