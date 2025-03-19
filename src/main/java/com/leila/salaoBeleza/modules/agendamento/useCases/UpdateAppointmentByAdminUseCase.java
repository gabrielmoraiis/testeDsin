package com.leila.salaoBeleza.modules.agendamento.useCases;

import com.leila.salaoBeleza.exceptions.ServicesNotFoundException;
import com.leila.salaoBeleza.modules.admin.dto.ServiceDTO;
import com.leila.salaoBeleza.modules.admin.repositories.ServicesRepository;
import com.leila.salaoBeleza.modules.agendamento.dto.AppointmentDTO;
import com.leila.salaoBeleza.modules.agendamento.entities.AppointmentEntity;
import com.leila.salaoBeleza.modules.agendamento.entities.UpdateAppointmentAdminDTO;
import com.leila.salaoBeleza.modules.agendamento.repositories.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class UpdateAppointmentByAdminUseCase {

    @Autowired
    private AppointmentRepository  appointmentRepository;

    @Autowired
    private ServicesRepository servicesRepository;

    public AppointmentDTO updateAppointment(UUID appointmentId, UpdateAppointmentAdminDTO updateDTO) throws Exception{
        AppointmentEntity appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new Exception("Agendamento não encontrado."));
        if(updateDTO.getAppointmentDate() != null ){
            LocalDateTime newDate = updateDTO.getAppointmentDate();
            if(newDate.isBefore(LocalDateTime.now())){
                throw new Exception("A data do agendamento deve ser atual ou posterior.");
            }
            int minute = newDate.getMinute();
            if(minute != 0 && minute != 30){
                throw new Exception("O horário do agendamento deve ser em intervalos de 30 minutos (ex: 15:00 ou 15:30).");
            }
            appointment.setAppointmentDate(newDate);
        }else {
            updateDTO.setAppointmentDate(appointment.getAppointmentDate());
        }

        if(updateDTO.getServiceId() != null){
            var service = servicesRepository.findById(updateDTO.getServiceId())
                    .orElseThrow(ServicesNotFoundException::new);
            appointment.setService(service);
            appointment.setClientId(service.getId());
        }else {
            updateDTO.setServiceId(appointment.getServiceId());
        }

        if(updateDTO.getStatus() != null && !updateDTO.getStatus().isEmpty()){
            if(updateDTO.getStatus().equals("AGENDADO") || updateDTO.getStatus().equals("CANCELADO") || updateDTO.getStatus().equals("Pendente")){
                appointment.setStatus(updateDTO.getStatus());
            }else {
                throw new Exception("O status deve ser um dos seguintes: AGENDADO, CANCELADO ou Pendente");
            }
        }
        appointmentRepository.save(appointment);
        return convertToDTO(appointment);
    }

    public AppointmentDTO convertToDTO(AppointmentEntity appointment) {
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
}
