package com.leila.salaoBeleza.modules.agendamento.controllers;
import com.leila.salaoBeleza.modules.agendamento.dto.AppointmentDTO;
import com.leila.salaoBeleza.modules.agendamento.entities.AppointmentEntity;
import com.leila.salaoBeleza.modules.agendamento.repositories.AppointmentRepository;
import com.leila.salaoBeleza.modules.agendamento.useCases.CreateAppointmentUseCase;
import com.leila.salaoBeleza.modules.agendamento.useCases.GetAppointmentsByPeriodUseCase;
import com.leila.salaoBeleza.modules.agendamento.useCases.MergeAppointmentUseCase;
import com.leila.salaoBeleza.modules.agendamento.useCases.UpdateCancelAppointmentByClientUseCase;
import com.leila.salaoBeleza.providers.JWTClientProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/client/appointment")
public class AppointmentController {

    @Autowired
    private CreateAppointmentUseCase createAppointmentUseCase;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private JWTClientProvider jwtClientProvider;

    @Autowired
    private GetAppointmentsByPeriodUseCase getAppointmentsByPeriodUseCase;

    @Autowired
    private UpdateCancelAppointmentByClientUseCase updateCancelAppointmentByClientUseCase;

    @Autowired
    private MergeAppointmentUseCase mergeAppointmentUseCase;

    @PostMapping("/new")
    @PreAuthorize("hasRole('CLIENT')")
    @Tag(name = "Cliente Agendamento", description = "Criação do Agendamento pelo Cliente")
    @Operation(summary = "Criação do Agendamento")
    public ResponseEntity<Object> createAppointment(HttpServletRequest request,
                                                    @Valid @RequestBody AppointmentDTO appointmentDTO,
                                                    @RequestParam(value = "mergeWithExisting", required = false) Boolean mergeWithExisting) {
        String authHeader = request.getHeader("Authorization");
        if(authHeader == null || authHeader.isBlank()){
            return ResponseEntity.badRequest().body("Token não informado.");
        }

        UUID clientId = jwtClientProvider.extractClientId(authHeader);
        appointmentDTO.setClientId(clientId);

        try {
            LocalDateTime requestedDate = appointmentDTO.getAppointmentDate();
            LocalDateTime startOfWeek = requestedDate.with(DayOfWeek.MONDAY)
                    .withHour(0).withMinute(0).withSecond(0).withNano(0);
            LocalDateTime endOfWeek = requestedDate.with(DayOfWeek.SUNDAY)
                    .withHour(23).withMinute(59).withSecond(59).withNano(999999999);

            List<AppointmentEntity> clientAppointments = appointmentRepository.findByClientIdAndAppointmentDateBetween(clientId, startOfWeek, endOfWeek);

            if (!clientAppointments.isEmpty() && mergeWithExisting != null && mergeWithExisting) {
                AppointmentEntity existingAppointment = clientAppointments.get(0);
                AppointmentDTO updatedAppointment = mergeAppointmentUseCase.mergeAppointment(clientId, appointmentDTO.getServiceId(), requestedDate);
                return ResponseEntity.ok(updatedAppointment);
            }

            AppointmentEntity appointment = createAppointmentUseCase.execute(
                    clientId,
                    appointmentDTO.getServiceId(),
                    appointmentDTO.getAppointmentDate()
            );

            return ResponseEntity.ok(appointment);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/")
    @PreAuthorize("hasRole('CLIENT')")
    @Tag(name = "Cliente Agendamento")
    @Operation(summary = "GET Todos os Agendamentos")
    public ResponseEntity<List<AppointmentEntity>> getAppointments(HttpServletRequest request) {
        Object clientIdObj = request.getAttribute("client_id");
        if (clientIdObj == null) {
            return ResponseEntity.badRequest().build();
        }
        UUID clientId = UUID.fromString(clientIdObj.toString());

        List<AppointmentEntity> appointments = appointmentRepository.findByClientId(clientId);
        return ResponseEntity.ok(appointments);
    }

    @GetMapping("/period")
    @PreAuthorize("hasRole('CLIENT')")
    @Tag(name = "Cliente Agendamento")
    @Operation(summary = "GET Agendamentos Dentro De Um Periodo")
    public ResponseEntity<List<AppointmentDTO>> getAppointmentsByPeriod(
            HttpServletRequest request,
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || authHeader.isBlank()) {
            return ResponseEntity.badRequest().build();
        }

        UUID clientId = jwtClientProvider.extractClientId(authHeader);
        List<AppointmentDTO> appointments = getAppointmentsByPeriodUseCase.execute(clientId, startDate, endDate);
        return ResponseEntity.ok(appointments);
    }

    @PutMapping("/update/{appointmentId}")
    @PreAuthorize("hasRole('CLIENT')")
    @Tag(name = "Cliente Agendamento")
    @Operation(summary = "UPDATE Agendamentos")
    public ResponseEntity<Object> updateAppointment(
            HttpServletRequest request,
            @PathVariable UUID appointmentId,
            @RequestParam("newDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime newDate) {

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || authHeader.isBlank()) {
            return ResponseEntity.badRequest().body("Token não informado.");
        }
        UUID clientId = jwtClientProvider.extractClientId(authHeader);

        try {
            AppointmentDTO appointment = updateCancelAppointmentByClientUseCase.updateAppointment(appointmentId, newDate, clientId);
            return ResponseEntity.ok(appointment);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/cancel/{appointmentId}")
    @PreAuthorize("hasRole('CLIENT')")
    @Tag(name = "Cliente Agendamento")
    @Operation(summary = "DELETE Agendamento")
    public ResponseEntity<Object> cancelAppointment(
            HttpServletRequest request,
            @PathVariable UUID appointmentId) {

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || authHeader.isBlank()) {
            return ResponseEntity.badRequest().body("Token não informado.");
        }
        UUID clientId = jwtClientProvider.extractClientId(authHeader);

        try {
            updateCancelAppointmentByClientUseCase.cancelAppointment(appointmentId, clientId);
            return null;
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
