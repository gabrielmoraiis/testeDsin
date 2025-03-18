package com.leila.salaoBeleza.modules.client.useCases;

import com.leila.salaoBeleza.exceptions.UserFoundException;
import com.leila.salaoBeleza.modules.client.ClientEntity;
import com.leila.salaoBeleza.modules.client.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CreateClientUseCase {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public ClientEntity execute(ClientEntity clientEntity){
        this.clientRepository
                .findByEmail(clientEntity.getEmail())
                .ifPresent((user) -> {
                    throw new UserFoundException();
                });
        var password = passwordEncoder.encode(clientEntity.getPassword());
        clientEntity.setPassword(password);
        return this.clientRepository.save(clientEntity);
    }
}
