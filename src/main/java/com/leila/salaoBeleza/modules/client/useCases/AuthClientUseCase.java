package com.leila.salaoBeleza.modules.client.useCases;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.leila.salaoBeleza.modules.client.ClientRepository;
import com.leila.salaoBeleza.modules.client.dto.AuthClientRequestDTO;
import com.leila.salaoBeleza.modules.client.dto.AuthClientResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;

@Service
public class AuthClientUseCase {

    @Value("${security.token.secret.client}")
    private String secretKey;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public AuthClientResponseDTO execute(AuthClientRequestDTO authClientRequestDTO) throws AuthenticationException{
        var client = this.clientRepository.findByEmail(authClientRequestDTO.email())
                .orElseThrow(() -> new RuntimeException("Um email/senha está incorreto"));
        var passwordMatches = this.passwordEncoder.matches(authClientRequestDTO.password(), client.getPassword());

        if(!passwordMatches){
            throw new AuthenticationException();
        }

        var roles =  Arrays.asList("CLIENT");
        Algorithm algorithm = Algorithm.HMAC256(secretKey);
        var expiresIn = Instant.now().plus(Duration.ofMinutes(10));
        var token = JWT.create()
                .withIssuer("salaobeleza")
                .withSubject(client.getId().toString())
                .withClaim("roles",roles)
                .withExpiresAt(expiresIn)
                .sign(algorithm);

        var authClientResponse = AuthClientResponseDTO.builder()
                .accessToken(token)
                .expires_in(expiresIn.toEpochMilli())
                .roles(roles)
                .build();
        return authClientResponse;
    }
}
