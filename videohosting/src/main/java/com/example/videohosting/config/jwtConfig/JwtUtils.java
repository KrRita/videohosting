package com.example.videohosting.config.jwtConfig;

import com.example.videohosting.model.UserModel;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtils {
    @Value("${videohosting.app.jwtSecret}")
    private String jwtSecret;

    @Value("${videohosting.app.jwtExpirationMs}")
    private int jwtExpirationMs;
    private final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    public String generateJwtToken(Authentication authentication) {
        logger.info("The beginning of token generation");
        UserModel userPrincipal = (UserModel) authentication.getPrincipal();

        return Jwts.builder()
                .claim("id", userPrincipal.getIdUser())
                .subject(userPrincipal.getEmail())
                .issuedAt(new Date())
                .expiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(getSigningKey())
                .compact();

    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String getEmailFromJwtToken(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public boolean validateJwtToken(String authToken) {
        logger.info("The beginning of token validation");
        try {
            Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(authToken);
            logger.info("Validation passed");
            return true;
        } catch (SignatureException e) {
            logger.error("A SignatureException occurred during verification");
        } catch (MalformedJwtException e) {
            logger.error("A MalformedJwtException occurred during verification");
        } catch (ExpiredJwtException e) {
            logger.error("A ExpiredJwtException occurred during verification");
        } catch (UnsupportedJwtException e) {
            logger.error("A UnsupportedJwtException occurred during verification");
        } catch (IllegalArgumentException e) {
            logger.error("A IllegalArgumentException occurred during verification");
        }
        return false;
    }
}
