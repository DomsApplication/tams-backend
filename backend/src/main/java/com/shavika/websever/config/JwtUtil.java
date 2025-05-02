package com.shavika.websever.config;

import com.shavika.websocket.utils.Utilities;
import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {


    public boolean isTokenValid(String token) {
        try {
            getAllClaims(token); // will throw if expired
            return true;
        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            // Token is expired
            System.out.println("JWT expired: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            // Invalid for other reasons
            return false;
        }
    }


    public String extractUsername(String token) throws Exception {
        return getAllClaims(token).getSubject(); // `sub` = userId
    }

    public Claims getAllClaims(String token) throws Exception {
        return Utilities.parseToken(token);
    }
}


