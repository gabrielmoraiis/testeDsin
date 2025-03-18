package com.leila.salaoBeleza.providers;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class JWTClientProvider {

    @Value("${security.token.secret.client}")
    private String secretKey;

    public DecodedJWT validateToken(String token){
        token = token.replace("Bearer ", "").trim();

        Algorithm algorithm = Algorithm.HMAC256(secretKey);
        try {
            return JWT.require(algorithm)
                    .build()
                    .verify(token);
        } catch (JWTVerificationException e) {
            e.printStackTrace();
            return null;
        }

    }

    public UUID extractClientId(String token) {
        DecodedJWT decodedJWT = validateToken(token);
        if(decodedJWT == null) {
            throw new JWTVerificationException("Token inválido ou expirado");
        }
        return UUID.fromString(decodedJWT.getSubject());
    }

}
