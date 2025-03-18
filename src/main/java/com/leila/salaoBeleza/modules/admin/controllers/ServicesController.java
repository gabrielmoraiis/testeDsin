package com.leila.salaoBeleza.modules.admin.controllers;

import com.leila.salaoBeleza.modules.admin.dto.CreateServiceDTO;
import com.leila.salaoBeleza.modules.admin.entities.ServicesEntity;
import com.leila.salaoBeleza.modules.admin.useCases.CreateServicesUseCase;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/admin/services")
public class ServicesController {

    @Autowired
    private CreateServicesUseCase createServicesUseCase;

    @PostMapping("/")
    @PreAuthorize( "hasRole('ADMIN')")
    public ServicesEntity create(@Valid @RequestBody CreateServiceDTO createServiceDTO, HttpServletRequest request){
        var adminId = request.getAttribute("admin_id");

        var servicesEntity = ServicesEntity.builder()
                .price(createServiceDTO.getPrice())
                .name(createServiceDTO.getName())
                .adminId(UUID.fromString(adminId.toString()))
                .description(createServiceDTO.getDescription())
                .duration(createServiceDTO.getDuration())
                .build();

        return this.createServicesUseCase.execute(servicesEntity);
    }

}
