package com.leila.salaoBeleza.modules.agendamento.controllers;

import com.leila.salaoBeleza.modules.agendamento.dto.AppointmentDTO;
import com.leila.salaoBeleza.modules.agendamento.entities.AppointmentEntity;
import com.leila.salaoBeleza.modules.agendamento.entities.UpdateAppointmentAdminDTO;
import com.leila.salaoBeleza.modules.agendamento.repositories.AppointmentRepository;
import com.leila.salaoBeleza.modules.agendamento.useCases.UpdateAppointmentByAdminUseCase;
import com.leila.salaoBeleza.providers.JWTClientProvider;
import jakarta.servlet.http.HttpServletRequest;
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
    public ResponseEntity<List<AppointmentDTO>> getAllAppointments() {
        List<AppointmentEntity> entities = appointmentRepository.listAllAppointments();
        List<AppointmentDTO> dtos = entities.stream().map(updateAppointmentByAdminUseCase::convertToDTO).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @PutMapping("/{appointmentId}/update")
    @PreAuthorize( "hasRole('ADMIN')")
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

    @DeleteMapping("/{appointmentId}/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> deleteAppointment(@PathVariable UUID appointmentId) {
        try {
            appointmentRepository.deleteById(appointmentId);
            return ResponseEntity.ok("Agendamento deletado com sucesso.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao deletar agendamento: " + e.getMessage());
        }
    }

}

