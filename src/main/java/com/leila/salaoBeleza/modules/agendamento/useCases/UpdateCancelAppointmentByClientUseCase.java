package com.leila.salaoBeleza.modules.agendamento.useCases;

import com.leila.salaoBeleza.modules.admin.dto.ServiceDTO;
import com.leila.salaoBeleza.modules.agendamento.dto.AppointmentDTO;
import com.leila.salaoBeleza.modules.agendamento.entities.AppointmentEntity;
import com.leila.salaoBeleza.modules.agendamento.repositories.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class UpdateCancelAppointmentByClientUseCase {

    @Autowired
    private AppointmentRepository appointmentRepository;

    private final String adminPhoneNumber = "(14) 99999-9999";

    public AppointmentDTO updateAppointment(UUID appointmentId, LocalDateTime newDate, UUID clientId) throws Exception {
        AppointmentEntity appointment = getAppointmentForClient(appointmentId, clientId);

        long diffDays = Duration.between(LocalDateTime.now(), appointment.getAppointmentDate()).toDays();

        if (diffDays < 2) {
            throw new Exception("Não é possível alterar o agendamento online, pois faltam menos de 2 dias. "
                    + "Por favor, entre em contato com o admin no telefone: " + adminPhoneNumber);
        }

        if (newDate.isBefore(LocalDateTime.now())) {
            throw new Exception("A nova data deve ser atual ou futura.");
        }

        int minute = newDate.getMinute();
        if (minute != 0 && minute != 30) {
            throw new Exception("O horário do agendamento deve ser em intervalos de 30 minutos (ex: 15:00 ou 15:30).");
        }

        appointment.setAppointmentDate(newDate);
        appointmentRepository.save(appointment);

        return convertToDTO(appointment);
    }

    private AppointmentDTO convertToDTO(AppointmentEntity appointment) {
        AppointmentDTO dto = new AppointmentDTO();
        dto.setId(appointment.getId());
        dto.setAppointmentDate(appointment.getAppointmentDate());
        dto.setClientId(appointment.getClientId());
        dto.setStatus(appointment.getStatus());
        dto.setCreatedAt(appointment.getCreatedAt());

        if (appointment.getService() != null) {
            ServiceDTO serviceDTO = new ServiceDTO();
            serviceDTO.setName(appointment.getService().getName());
            serviceDTO.setPrice(appointment.getService().getPrice());
            serviceDTO.setDescription(appointment.getService().getDescription());
            serviceDTO.setDuration(appointment.getService().getDuration());
            dto.setService(serviceDTO);
        }

        return dto;
    }

    public void cancelAppointment(UUID appointmentId, UUID clientId) throws Exception {
        AppointmentEntity appointment = getAppointmentForClient(appointmentId, clientId);

        long diffDays = Duration.between(LocalDateTime.now(), appointment.getAppointmentDate()).toDays();
        if (diffDays < 2) {
            throw new Exception("Não é possível cancelar o agendamento online, pois faltam menos de 2 dias. "
                    + "Por favor, entre em contato com o admin no telefone: " + adminPhoneNumber);
        }

        appointmentRepository.delete(appointment);
    }


    private AppointmentEntity getAppointmentForClient(UUID appointmentId, UUID clientId) throws Exception {
        Optional<AppointmentEntity> optionalAppointment = appointmentRepository.findById(appointmentId);
        if (optionalAppointment.isEmpty()) {
            throw new Exception("Agendamento não encontrado.");
        }
        AppointmentEntity appointment = optionalAppointment.get();
        if (!appointment.getClientId().equals(clientId)) {
            throw new Exception("Acesso negado. Esse agendamento não pertence ao cliente informado.");
        }
        return appointment;
    }
}
