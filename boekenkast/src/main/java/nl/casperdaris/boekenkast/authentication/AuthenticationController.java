package nl.casperdaris.boekenkast.authentication;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

// TODO: Add documentation
@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Authentication endpoints")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> registerUser(@RequestBody @Valid RegisterUserRequest registerUserRequest)
            throws MessagingException {
        authenticationService.registerUser(registerUserRequest);
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticateUserResponse> authenticateUser(
            @RequestBody @Valid AuthenticateUserRequest authenticateUserRequest) throws MessagingException {
        return ResponseEntity.ok(authenticationService.authenticate(authenticateUserRequest));
    }

    @GetMapping("/activate-account")
    public void activateAccount(@RequestParam String token) throws MessagingException {
        authenticationService.activateAccount(token);
    }
}
