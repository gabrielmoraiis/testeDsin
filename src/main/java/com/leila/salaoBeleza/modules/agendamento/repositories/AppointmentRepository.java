package com.leila.salaoBeleza.modules.agendamento.repositories;

import com.leila.salaoBeleza.modules.agendamento.entities.AppointmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AppointmentRepository extends JpaRepository<AppointmentEntity, UUID> {

    @Query("SELECT a FROM tb_appointment a ORDER BY a.appointmentDate ASC")
    List<AppointmentEntity> listAllAppointments();
    List<AppointmentEntity> findByClientId(UUID clientId);
    Optional<AppointmentEntity> findByAppointmentDate(LocalDateTime appointmentDate);
    List<AppointmentEntity> findByClientIdAndAppointmentDateBetween(UUID clientId, LocalDateTime start, LocalDateTime end);
    List<AppointmentEntity> findByClientIdAndCreatedAtBetween(UUID clientId, LocalDateTime start, LocalDateTime end);
    void deleteAppointmentById(@Param("id") UUID id);

}

