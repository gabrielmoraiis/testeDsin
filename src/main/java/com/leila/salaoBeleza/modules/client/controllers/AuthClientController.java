package com.leila.salaoBeleza.modules.client.controllers;

import com.leila.salaoBeleza.modules.client.dto.AuthClientRequestDTO;
import com.leila.salaoBeleza.modules.client.useCases.AuthClientUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/client")
public class AuthClientController {

    @Autowired
    private AuthClientUseCase authClientUseCase;

    @PostMapping("/auth")
    @Tag(name = "Cliente")
    @Operation(summary = "Autenticação do Cliente")
    public ResponseEntity<Object> auth(@RequestBody AuthClientRequestDTO authClientRequestDTO){
        try {
            var token = this.authClientUseCase.execute(authClientRequestDTO);
            return ResponseEntity.ok(token);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }
}
