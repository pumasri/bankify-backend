package seniorproject.bankifycore.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import seniorproject.bankifycore.domain.User;

import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Service
public class JwtTokenService {

    @Value("${security.jwt.secret:change-me-in-prod-change-me-in-prod-change-me}")
    private String secret;

    @Value("${security.jwt.expiration-seconds:3600}")
    private long expirationSeconds;

    @Value("${security.jwt.atm-expiration-seconds:180}")
    private long atmExpirationSeconds;

    @Value("${security.jwt.partner-portal-expiration-seconds:3600}")
    private long partnerPortalExpirationSeconds;

    private Key key;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    // existing humans
    public String generateToken(User user) {
        Instant now = Instant.now();
        Instant expiry = now.plusSeconds(expirationSeconds);

        return Jwts.builder()
                .setSubject(user.getId().toString())
                .claim("typ", "USER")
                .claim("email", user.getEmail())
                .claim("role", user.getRole())
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(expiry))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateAtmToken(UUID accountId) {
        Instant now = Instant.now();
        Instant expiry = now.plusSeconds(atmExpirationSeconds);

        return Jwts.builder()
                .setSubject("ATM")
                .claim("typ", "ATM")
                .claim("atmAccountId", accountId.toString())
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(expiry))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }


    public String generatePartnerPortalToken(UUID partnerUserId, String email, String role) {
        Instant now = Instant.now();
        Instant expiry = now.plusSeconds(partnerPortalExpirationSeconds);

        return Jwts.builder()
                .setSubject(partnerUserId.toString())
                .claim("typ", "PARTNER_PORTAL")
                .claim("email", email)
                .claim("role", role) // OWNER / MEMBER
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(expiry))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }


    public Claims parseToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public UUID extractAtmAccountId(Claims claims) {
        Object v = claims.get("atmAccountId");
        if (v == null)
            throw new IllegalStateException("Missing atmAccountId claim");
        return UUID.fromString(v.toString());
    }

    public boolean isAtmToken(Claims claims) {
        return "ATM".equals(String.valueOf(claims.get("typ")));
    }

}
