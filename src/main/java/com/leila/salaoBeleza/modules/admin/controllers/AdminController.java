package com.leila.salaoBeleza.modules.admin.controllers;

import com.leila.salaoBeleza.modules.admin.entities.AdminEntity;
import com.leila.salaoBeleza.modules.admin.useCases.CreateAdminUseCase;
import com.leila.salaoBeleza.modules.agendamento.dto.AppointmentDTO;
import com.leila.salaoBeleza.modules.agendamento.entities.UpdateAppointmentAdminDTO;
import com.leila.salaoBeleza.modules.agendamento.useCases.UpdateAppointmentByAdminUseCase;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private CreateAdminUseCase createAdminUseCase;

    @RequestMapping("/")
    public ResponseEntity<Object> create(@Valid @RequestBody AdminEntity adminEntity){
        try{
            var result = this.createAdminUseCase.execute(adminEntity);
            return ResponseEntity.ok(result);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}


