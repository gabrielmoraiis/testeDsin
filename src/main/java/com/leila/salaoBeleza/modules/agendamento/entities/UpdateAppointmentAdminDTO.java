package com.leila.salaoBeleza.modules.agendamento.entities;


import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class UpdateAppointmentAdminDTO {
    private LocalDateTime appointmentDate;
    private UUID serviceId;
    private String status;
}