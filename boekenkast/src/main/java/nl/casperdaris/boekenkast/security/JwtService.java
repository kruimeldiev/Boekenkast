package nl.casperdaris.boekenkast.security;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

/// Deze service bevat alle functies die het werken met tokens doen, 
/// zoals het genereren van tokens, het controleren of een token geldig is
/// en het controleren of een token verlopen is
@Service
public class JwtService {

    @Value("${application.security.jwt.expiration}")
    private long jwtExpiration;

    @Value("${application.security.jwt.secret-key}")
    private String secretKey;

    /// Genereer een token op basis van user details, zonder extra claims
    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    /// Genereer een token op basis van user details met extra claims
    /// Extra claims zijn extra gegevens die aan het token kunnen worden toegevoegd,
    /// bijvoorbeeld specifieke gebruikersrechten of applicatie-informatie
    public String generateToken(Map<String, Object> claims, UserDetails userDetails) {
        return buildToken(claims, userDetails, jwtExpiration);
    }

    /// Deze methode doet het daadwerkelijke werk van het bouwen van het token
    /// Door deze taken te isoleren in een aparte methode,
    /// wordt de logica rond tokenconstructie gescheiden van de andere methoden,
    /// wat de leesbaarheid en het onderhoud van de code vergroot
    private String buildToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails,
            long jwtExpiration) {
        var authorities = userDetails
                .getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .claim("authorities", authorities)
                .signWith(getSignInKey())
                .compact();
    }

    /// Controlleer of een token nog geldig is
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String userEmail = extractUserEmail(token);
        return (userEmail.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    /// Controleer of het token verlopen is door de vervaldatum van het token te
    /// vergelijken met de huidige datum
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /// Extracteer de vervaldatum uit het token
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /// Extracteer de email uit het token
    public String extractUserEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /// Extracteer een claim uit het token
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /// Extracteer alle claims uit het token
    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /// Genereer een nieuw token met een nieuwe vervaldatum
    private Key getSignInKey() {
        return Keys.secretKeyFor(SignatureAlgorithm.HS256);
    }
}
