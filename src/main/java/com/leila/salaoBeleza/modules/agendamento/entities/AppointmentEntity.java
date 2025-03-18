package com.leila.salaoBeleza.modules.agendamento.entities;

import com.leila.salaoBeleza.modules.admin.entities.AdminEntity;
import com.leila.salaoBeleza.modules.admin.entities.ServicesEntity;
import com.leila.salaoBeleza.modules.client.ClientEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name = "tb_appointment")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne()
    @JoinColumn(name = "client_id", insertable = false, updatable = false)
    private ClientEntity client;

    @Column(name = "client_id", nullable = false)
    private UUID clientId;

    @ManyToOne()
    @JoinColumn(name = "service_id", insertable = false, updatable = false)
    private ServicesEntity service;

    @Column(name = "service_id", nullable = false)
    private UUID serviceId;

    @FutureOrPresent(message = "A data do agendamento deve ser atual ou futura")
    private LocalDateTime appointmentDate;

    @Builder.Default
    @Column(nullable = false)
    private String status = "Pendente";

    @CreationTimestamp
    private LocalDateTime createdAt;



}
