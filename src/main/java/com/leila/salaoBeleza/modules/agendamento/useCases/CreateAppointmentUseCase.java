package com.leila.salaoBeleza.modules.agendamento.useCases;

import com.leila.salaoBeleza.exceptions.ServicesNotFoundException;
import com.leila.salaoBeleza.exceptions.UserNotFoundException;
import com.leila.salaoBeleza.modules.admin.repositories.ServicesRepository;
import com.leila.salaoBeleza.modules.agendamento.entities.AppointmentEntity;
import com.leila.salaoBeleza.modules.agendamento.repositories.AppointmentRepository;
import com.leila.salaoBeleza.modules.client.ClientEntity;
import com.leila.salaoBeleza.modules.client.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CreateAppointmentUseCase {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ServicesRepository servicesRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    public AppointmentEntity execute(UUID idClient, UUID idServices, LocalDateTime appointmentDate) throws IllegalAccessException {

        if (appointmentDate.isBefore(LocalDateTime.now())) {
            throw new IllegalAccessException("A data do agendamento deve ser atual ou posterior.");
        }

        int minute = appointmentDate.getMinute();
        if (minute != 0 && minute != 30) {
            throw new IllegalArgumentException("O horário do agendamento deve ser em intervalos de 30 minutos (ex: 15:00 ou 15:30).");
        }

        if (appointmentRepository.findByAppointmentDate(appointmentDate).isPresent()) {
            throw new IllegalArgumentException("Já existe um agendamento para este horário.");
        }

        ClientEntity client = clientRepository.findById(idClient)
                .orElseThrow(UserNotFoundException::new);
        var service = servicesRepository.findById(idServices)
                .orElseThrow(ServicesNotFoundException::new);


        AppointmentEntity appointment = AppointmentEntity.builder()
                .clientId(idClient)
                .serviceId(idServices)
                .appointmentDate(appointmentDate)
                .build();

        appointment.setClient(client);
        appointment.setService(service);

        appointmentRepository.save(appointment);

        return appointment;
    }
}
