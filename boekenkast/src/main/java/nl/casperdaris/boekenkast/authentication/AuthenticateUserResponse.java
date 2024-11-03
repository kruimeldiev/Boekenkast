package nl.casperdaris.boekenkast.authentication;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

// TODO: Add documentation
@Getter
@Setter
@Builder
public class AuthenticateUserResponse {
    private String token;
}
