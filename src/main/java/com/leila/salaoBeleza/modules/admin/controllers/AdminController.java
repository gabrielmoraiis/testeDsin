package com.leila.salaoBeleza.modules.admin.controllers;

import com.leila.salaoBeleza.modules.admin.entities.AdminEntity;
import com.leila.salaoBeleza.modules.admin.useCases.CreateAdminUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private CreateAdminUseCase createAdminUseCase;

    @PostMapping("/")
    @Tag(name = "Admin", description = "Informações do admin")
    @Operation(summary = "Criação do admin")
    public ResponseEntity<Object> create(@Valid @RequestBody AdminEntity adminEntity){
        try{
            var result = this.createAdminUseCase.execute(adminEntity);
            return ResponseEntity.ok(result);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}


