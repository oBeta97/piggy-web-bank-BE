package oBeta.PiggyWebBank.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import oBeta.PiggyWebBank.entities.User;
import oBeta.PiggyWebBank.exceptions.UnauthorizedException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JWT {
    @Value("${jwt.secret}") // Il segreto sarà salvato in env.properties e letto da application.properties
    private String secret;

    public String createToken(User user) {
        return Jwts.builder()
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 7))
                .subject(String.valueOf(user.getId()))
                .signWith(Keys.hmacShaKeyFor(secret.getBytes()))
                .compact();
    }

    public void verifyToken(String accessToken) {
        try {
            Jwts.parser()
                    .verifyWith(Keys.hmacShaKeyFor(secret.getBytes()))
                    .build()
                    .parse(accessToken);
        } catch (Exception ex) {
            throw new UnauthorizedException("Invalid Token!");
        }
    }

    public String getIdFromToken(String accessToken) {
        return Jwts.
                parser().
                verifyWith(Keys.hmacShaKeyFor(secret.getBytes())).
                build().
                parseSignedClaims(accessToken).
                getPayload().
                getSubject();
    }
}