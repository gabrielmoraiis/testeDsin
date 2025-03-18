package com.leila.salaoBeleza.modules.admin.useCases;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.leila.salaoBeleza.modules.admin.dto.AuthAdminDTO;
import com.leila.salaoBeleza.modules.admin.dto.AuthAdminResponseDTO;
import com.leila.salaoBeleza.modules.admin.repositories.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import org.springframework.security.core.AuthenticationException;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;

@Service
public class AuthAdminUseCase {

    @Value("${security.token.secret}")
    private String secretKey;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public AuthAdminResponseDTO execute(AuthAdminDTO authAdminDTO){
        var admin = this.adminRepository.findByEmail(authAdminDTO.getEmail()).orElseThrow(
                () -> {
                    throw new RuntimeException("Um email/senha está incorreto");
                });

        var passwordMatches = this.passwordEncoder.matches(authAdminDTO.getPassword(), admin.getPassword());

        if(!passwordMatches){
            throw new AuthenticationException("Credenciais inválidas.") {};
        }

        Algorithm algorithm = Algorithm.HMAC256(secretKey);

        var expiresIn = Instant.now().plus(Duration.ofHours(2));

        var token = JWT.create().withIssuer("salaoBeleza")
                .withExpiresAt(expiresIn)
                .withSubject(admin.getId().toString())
                .withClaim("roles", Arrays.asList("ADMIN"))
                .sign(algorithm);

        var authAdminResponseDTO = AuthAdminResponseDTO.builder()
                .access_token(token)
                .expires_in(expiresIn.toEpochMilli())
                .build();
        return authAdminResponseDTO;
    }
}
