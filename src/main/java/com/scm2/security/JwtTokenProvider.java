package com.scm2.security;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.scm2.exception.BlogException;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtTokenProvider {

    @Value("${app.jwt-secret}")
    private String jwtSecret;

    @Value("${app-jwt-expiration-milliseconds}")
    private long jwtExpirationDate;

    // generate jwt token

    public String generateToken(Authentication authentication) {
        String userName = authentication.getName();
        Date currentdate = new Date();
        Date expireDate = new Date(currentdate.getTime() + jwtExpirationDate);

        return Jwts.builder().subject(userName).issuedAt(new Date()).expiration(expireDate).signWith(key()).compact();

    }

    // generate secret key
    private SecretKey key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    // get username from token
    public String getUsername(String token) {
        return Jwts.parser().verifyWith(key()).build().parseSignedClaims(token).getPayload().getSubject();

    }

    // validate jwt token

    public boolean validateToken(String token) {
        try {
            Jwts.parser().verifyWith(key()).build().parse(token);
            return true;
        } catch (MalformedJwtException e) {
            throw new BlogException("Invalid jwt token", HttpStatus.BAD_REQUEST);
        } catch (ExpiredJwtException exception) {
            throw new BlogException("Expired jwt token", HttpStatus.BAD_REQUEST);
        } catch (UnsupportedJwtException unsupportedJwtException) {
            throw new BlogException("Unsupported JWT Token", HttpStatus.BAD_REQUEST);
        } catch (IllegalArgumentException illegalArgumentException) {
            throw new BlogException("Jwt claims string is null or empty", HttpStatus.BAD_REQUEST);
        }

    }

}
