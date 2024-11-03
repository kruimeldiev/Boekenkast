package nl.casperdaris.boekenkast.security;

import nl.casperdaris.boekenkast.user.UserRepository;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

/// De UserDetailsServiceImpl klasse is een service die gebruikersinformatie laadt uit 
/// de database op basis van een gebruikers email. 
/// Het is een implementatie van de UserDetailsService interface
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    /// Laad de gebruikersinformatie op basis van de gebruikers email uit de
    /// database en gebruik deze om een UserDetails object te genereren.
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
        return userRepository
                .findByUsername(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("Email not found: " + userEmail));
    }
}
