package com.leila.salaoBeleza.modules.agendamento.useCases;

import com.leila.salaoBeleza.exceptions.ServicesNotFoundException;
import com.leila.salaoBeleza.modules.admin.dto.ServiceDTO;
import com.leila.salaoBeleza.modules.admin.entities.ServicesEntity;
import com.leila.salaoBeleza.modules.admin.repositories.ServicesRepository;
import com.leila.salaoBeleza.modules.agendamento.dto.AppointmentDTO;
import com.leila.salaoBeleza.modules.agendamento.entities.AppointmentEntity;
import com.leila.salaoBeleza.modules.agendamento.repositories.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class MergeAppointmentUseCase {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private ServicesRepository servicesRepository;


    public AppointmentDTO mergeAppointment(UUID clientId, UUID newServiceId, LocalDateTime requestedDate) throws Exception {
        LocalDateTime startOfWeek = requestedDate.with(DayOfWeek.MONDAY)
                .withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime endOfWeek = requestedDate.with(DayOfWeek.SUNDAY)
                .withHour(23).withMinute(59).withSecond(59).withNano(999999999);

        List<AppointmentEntity> clientAppointments = appointmentRepository.findByClientIdAndAppointmentDateBetween(clientId, startOfWeek, endOfWeek);
        if (clientAppointments.isEmpty()) {
            throw new Exception("Não há agendamentos existentes na semana para mesclagem.");
        }

        ServiceDTO serviceDTO = new ServiceDTO();

        AppointmentEntity existingAppointment = clientAppointments.get(0);

        ServicesEntity newService = servicesRepository.findById(newServiceId)
                .orElseThrow(() -> new ServicesNotFoundException());

        String currentServiceName = existingAppointment.getService().getName();
        BigDecimal currentServicePrice = existingAppointment.getService().getPrice();
        String currentServiceDescription = existingAppointment.getService().getDescription();


        String mergedName = currentServiceName + " e " + newService.getName();
        BigDecimal mergedPrice = currentServicePrice.add(newService.getPrice());
        String mergedDescription = currentServiceDescription + " e " + newService.getDescription();

        newService.setName(mergedName);
        newService.setPrice(mergedPrice);
        newService.setDescription(mergedDescription);

        existingAppointment.setService(newService);
        existingAppointment.setServiceId(newService.getId());

        appointmentRepository.save(existingAppointment);

        return convertToDTO(existingAppointment, mergedName, mergedPrice, mergedDescription);
    }

    private AppointmentDTO convertToDTO(AppointmentEntity appointment, String mergedName, BigDecimal mergedPrice, String mergedDescription) {


        AppointmentDTO dto = new AppointmentDTO();
        dto.setId(appointment.getId());
        dto.setAppointmentDate(appointment.getAppointmentDate());
        dto.setClientId(appointment.getClientId());
        dto.setStatus(appointment.getStatus());
        dto.setCreatedAt(appointment.getCreatedAt());

        ServiceDTO serviceDTO = new ServiceDTO();
        serviceDTO.setName(mergedName);
        serviceDTO.setPrice(mergedPrice);
        serviceDTO.setDescription(mergedDescription);
        serviceDTO.setDuration(appointment.getService() != null ? appointment.getService().getDuration() : null);
        dto.setService(serviceDTO);
        return dto;
    }
}
