package com.leila.salaoBeleza.modules.client.useCases;

import com.leila.salaoBeleza.exceptions.ServicesNotFoundException;
import com.leila.salaoBeleza.exceptions.UserNotFoundException;
import com.leila.salaoBeleza.modules.admin.entities.ServicesEntity;
import com.leila.salaoBeleza.modules.admin.repositories.ServicesRepository;
import com.leila.salaoBeleza.modules.agendamento.entities.AppointmentEntity;
import com.leila.salaoBeleza.modules.agendamento.repositories.AppointmentRepository;
import com.leila.salaoBeleza.modules.agendamento.useCases.CreateAppointmentUseCase;
import com.leila.salaoBeleza.modules.client.ClientEntity;
import com.leila.salaoBeleza.modules.client.ClientRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.OngoingStubbing;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CreateAppointmentUseCaseTest {

    @InjectMocks
    private CreateAppointmentUseCase createAppointmentUseCase;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private ServicesRepository servicesRepository;

    @Mock
    private AppointmentRepository appointmentRepository;

    @Test
    @DisplayName("Should not be able to create appointment with client not found")
    public void should_not_be_able_to_create_appointment_with_client_not_found(){
        try {
            createAppointmentUseCase.execute(null, null,  null);
        }catch (Exception e){
            Assertions.assertThat(e).isInstanceOf(UserNotFoundException.class);
        }
    }

    @Test
    public void should_not_be_able_to_create_appointment_with_services_not_found(){
        var idClient = UUID.randomUUID();

        var client = new ClientEntity();
        client.setId(idClient);

        when(clientRepository.findById(idClient)).thenReturn(Optional.of(client));

        try {
            createAppointmentUseCase.execute(idClient, null, null);
        }catch (Exception e){
            Assertions.assertThat(e).isInstanceOf(ServicesNotFoundException.class);
        }
    }

    @Test
    @DisplayName("Should not be able to create appointment without date")
    public void should_not_be_able_to_create_appointment_without_date() {
        UUID idClient = UUID.randomUUID();
        UUID idService = UUID.randomUUID();

        ClientEntity client = new ClientEntity();
        client.setId(idClient);
       lenient().when(clientRepository.findById(idClient)).thenReturn(Optional.of(client));

        ServicesEntity service = new ServicesEntity();
        service.setId(idService);
        lenient().when(servicesRepository.findById(idService)).thenReturn(Optional.of(service));

        Assertions.assertThatThrownBy(() ->
                createAppointmentUseCase.execute(idClient, idService, null)
        ).isInstanceOf(NullPointerException.class);
    }

    @Test
    public void should_be_able_to_create_appointment() throws IllegalAccessException {
        var idClient = UUID.randomUUID();
        var idService = UUID.randomUUID();
        var idAppointment = UUID.randomUUID();
        var appointmentDate = LocalDateTime.of(2025, 3, 17, 15, 0);

        ClientEntity client = new ClientEntity();
        client.setId(idClient);
        when(clientRepository.findById(idClient)).thenReturn(Optional.of(new ClientEntity()));

        ServicesEntity service = new ServicesEntity();
        service.setId(idService);
        when(servicesRepository.findById(idService)).thenReturn(Optional.of(new ServicesEntity()));

        var appointment = AppointmentEntity.builder().clientId(idClient).serviceId(idService).appointmentDate(appointmentDate).id(idAppointment).build();

        when(appointmentRepository.save(any(AppointmentEntity.class))).thenReturn(appointment);

        var result = createAppointmentUseCase.execute(idClient, idService, appointmentDate);

        Assertions.assertThat(result).hasFieldOrProperty("id");
    }

}
