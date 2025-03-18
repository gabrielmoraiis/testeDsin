package com.leila.salaoBeleza.modules.agendamento.dto;

import com.leila.salaoBeleza.modules.admin.dto.ServiceDTO;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class AppointmentDTO {

    @NotNull(message = "A data do agendamento é obrigatória.")
    @FutureOrPresent(message = "O agendamento deve ser para o dia atual ou posterior.")
    private LocalDateTime appointmentDate;

    @NotNull(message = "O serviço é obrigatório.")
    private UUID serviceId;

    private UUID clientId;
    private String status;
    private UUID id;
    private LocalDateTime createdAt;

    private ServiceDTO service;

}
