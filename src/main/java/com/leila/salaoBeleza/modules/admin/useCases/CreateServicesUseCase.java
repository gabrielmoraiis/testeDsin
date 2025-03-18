package com.leila.salaoBeleza.modules.admin.useCases;

import com.leila.salaoBeleza.modules.admin.entities.ServicesEntity;
import com.leila.salaoBeleza.modules.admin.repositories.ServicesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreateServicesUseCase {

    @Autowired
    private ServicesRepository servicesRepository;

    public ServicesEntity execute(ServicesEntity servicesEntity){
        return this.servicesRepository.save(servicesEntity);
    }
}