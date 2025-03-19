package com.leila.salaoBeleza.modules.agendamento.controllers;

import com.leila.salaoBeleza.modules.agendamento.dto.AppointmentDTO;
import com.leila.salaoBeleza.modules.agendamento.entities.AppointmentEntity;
import com.leila.salaoBeleza.modules.agendamento.entities.UpdateAppointmentAdminDTO;
import com.leila.salaoBeleza.modules.agendamento.repositories.AppointmentRepository;
import com.leila.salaoBeleza.modules.agendamento.useCases.UpdateAppointmentByAdminUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin/appointment")
public class AppointmentAdminController {

    @Autowired
    private UpdateAppointmentByAdminUseCase updateAppointmentByAdminUseCase;
    @Autowired
    private AppointmentRepository appointmentRepository;

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    @Tag(name = "Admin Agendamento")
    @Operation(summary = "Lista de Agendamentos")
    public ResponseEntity<List<AppointmentDTO>> getAllAppointments() {
        List<AppointmentEntity> entities = appointmentRepository.listAllAppointments();
        List<AppointmentDTO> dtos = entities.stream().map(updateAppointmentByAdminUseCase::convertToDTO).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @PutMapping("update/{appointmentId}")
    @PreAuthorize( "hasRole('ADMIN')")
    @Tag(name = "Admin Agendamento")
    @Operation(summary = "Atualizar um Agendamento")
    public ResponseEntity<Object> updateAppointmentByAdmin(
            @PathVariable UUID appointmentId,
            @RequestBody UpdateAppointmentAdminDTO updateDTO ){
        try {
            AppointmentDTO updatedAppointment = updateAppointmentByAdminUseCase.updateAppointment(appointmentId, updateDTO);
            return ResponseEntity.ok(updatedAppointment);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("delete/{appointmentId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Tag(name = "Admin Agendamento")
    @Operation(summary = "Deletar um Agendamento")
    public ResponseEntity<Object> deleteAppointment(@PathVariable UUID appointmentId) {
        try {
            appointmentRepository.deleteById(appointmentId);
            return ResponseEntity.ok("Agendamento deletado com sucesso.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao deletar agendamento: " + e.getMessage());
        }
    }

}

