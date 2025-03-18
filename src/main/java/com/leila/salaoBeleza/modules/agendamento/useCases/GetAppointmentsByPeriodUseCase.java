package com.leila.salaoBeleza.modules.agendamento.useCases;

import com.leila.salaoBeleza.modules.admin.dto.ServiceDTO;
import com.leila.salaoBeleza.modules.agendamento.dto.AppointmentDTO;
import com.leila.salaoBeleza.modules.agendamento.entities.AppointmentEntity;
import com.leila.salaoBeleza.modules.agendamento.repositories.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class GetAppointmentsByPeriodUseCase {

    @Autowired
    private AppointmentRepository appointmentRepository;

    public List<AppointmentDTO> execute(UUID clientId, LocalDateTime startDate, LocalDateTime endDate) {
        List<AppointmentEntity> entities = appointmentRepository.findByClientIdAndCreatedAtBetween(clientId, startDate, endDate);
        return entities.stream().map(entity -> {
            AppointmentDTO dto = new AppointmentDTO();
            dto.setId(entity.getId());
            dto.setAppointmentDate(entity.getAppointmentDate());
            dto.setClientId(entity.getClientId());
            dto.setServiceId(entity.getServiceId());
            dto.setStatus(entity.getStatus());
            dto.setCreatedAt(entity.getCreatedAt());

            if (entity.getService() != null) {
                ServiceDTO serviceDTO = new ServiceDTO();
                serviceDTO.setName(entity.getService().getName());
                serviceDTO.setPrice(entity.getService().getPrice());
                serviceDTO.setDescription(entity.getService().getDescription());
                serviceDTO.setDuration(entity.getService().getDuration());
                dto.setService(serviceDTO);
            }
            return dto;
        }).collect(Collectors.toList());
    }
}
