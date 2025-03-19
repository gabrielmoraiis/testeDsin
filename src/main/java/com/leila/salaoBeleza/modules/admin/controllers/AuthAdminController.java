package com.leila.salaoBeleza.modules.admin.controllers;

import com.leila.salaoBeleza.modules.admin.dto.AuthAdminDTO;
import com.leila.salaoBeleza.modules.admin.useCases.AuthAdminUseCase;
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
@RequestMapping("/admin")
public class AuthAdminController {

    @Autowired
    private AuthAdminUseCase authAdminUseCase;

    @PostMapping("/auth")
    @Tag(name = "Admin")
    @Operation(summary = "Autenticação do Admin")
    public ResponseEntity<Object> create(@RequestBody AuthAdminDTO authAdminDTO) {

        try {
            var result = this.authAdminUseCase.execute(authAdminDTO);
            return ResponseEntity.ok(result);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }
}
