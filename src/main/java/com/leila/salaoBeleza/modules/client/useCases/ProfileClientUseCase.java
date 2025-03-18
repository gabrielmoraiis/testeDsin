package com.leila.salaoBeleza.modules.client.useCases;

import com.leila.salaoBeleza.modules.client.ClientRepository;
import com.leila.salaoBeleza.modules.client.dto.ProfileClientResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ProfileClientUseCase {
    @Autowired
    private ClientRepository clientRepository;

    public ProfileClientResponseDTO execute(UUID idClient) {
        var client = this.clientRepository.findById(idClient)
                .orElseThrow(() ->{
                    throw new UsernameNotFoundException("User not found");
                });
        var clientDTO = ProfileClientResponseDTO.builder()
                .name(client.getName())
                .email(client.getEmail())
                .cellPhoneNumber(client.getCellPhoneNumber())
                .id(client.getId())
                .build();

        return clientDTO;
    }



}
