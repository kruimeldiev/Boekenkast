package nl.casperdaris.boekenkast.authentication;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import nl.casperdaris.boekenkast.email.EmailService;
import nl.casperdaris.boekenkast.email.EmailTemplateName;
import nl.casperdaris.boekenkast.role.RoleRepository;
import nl.casperdaris.boekenkast.user.Token;
import nl.casperdaris.boekenkast.user.TokenRepository;
import nl.casperdaris.boekenkast.user.User;
import nl.casperdaris.boekenkast.user.UserRepository;

// TODO: Add documentation
@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final EmailService emailService;

    @Value("${application.mailing.frontend.activation-url}")
    private String activationUrl;

    public void registerUser(RegisterUserRequest registerUserRequest) throws MessagingException {
        var userRole = roleRepository
                .findByName("USER")
                .orElseThrow(() -> new IllegalStateException("USER role not found"));
        var user = User.builder()
                .username(registerUserRequest.getUsername())
                // .dateOfBirth(registerUserRequest.getDateOfBirth())
                .email(registerUserRequest.getEmail())
                .password(passwordEncoder.encode(registerUserRequest.getPassword()))
                .accountLocked(false)
                .enabled(false)
                .roles(List.of(userRole))
                .build();
        userRepository.save(user);
        sendValidationEmail(user);
    }

    private void sendValidationEmail(User user) throws MessagingException {
        var newToken = generateAndSaveActivationToken(user);
        emailService.sendEmail(
                user.getEmail(),
                user.getUsername(),
                EmailTemplateName.ACTIVATE_ACCOUNT,
                activationUrl,
                newToken,
                "Activate your account for Boekenkast");
    }

    private String generateAndSaveActivationToken(User user) {
        String generatedTokenString = generateActivationToken(6);
        var token = Token.builder()
                .token(generatedTokenString)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusHours(1))
                .user(user)
                .build();
        tokenRepository.save(token);
        return generatedTokenString;
    }

    private String generateActivationToken(int length) {
        String characters = "0123456789";
        StringBuilder sb = new StringBuilder();
        SecureRandom secureRandom = new SecureRandom();
        for (int i = 0; i < length; i++) {
            sb.append(characters.charAt(secureRandom.nextInt(characters.length())));
        }
        return sb.toString();
    }
}
