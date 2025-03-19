package com.leila.salaoBeleza.modules.client.controllers;

import com.leila.salaoBeleza.modules.admin.dto.ServiceDTO;
import com.leila.salaoBeleza.modules.admin.entities.ServicesEntity;
import com.leila.salaoBeleza.modules.admin.repositories.ServicesRepository;
import com.leila.salaoBeleza.modules.client.ClientEntity;
import com.leila.salaoBeleza.exceptions.UserFoundException;
import com.leila.salaoBeleza.modules.client.useCases.CreateClientUseCase;
import com.leila.salaoBeleza.modules.client.useCases.ProfileClientUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/client")
public class ClientController {

    @Autowired
    private CreateClientUseCase createClientUseCase;

    @Autowired
    private ProfileClientUseCase profileClientUseCase;

    @Autowired
    private ServicesRepository servicesRepository;

    @PostMapping("/")
    @Tag(name = "Cliente", description = "Informações do cliente")
    @Operation(summary = "Criação do cliente")
    public ResponseEntity<Object> create(@Valid @RequestBody ClientEntity clientEntity) {
      try{
            var result = this.createClientUseCase.execute(clientEntity);
            return ResponseEntity.ok(result);
      }catch (Exception e){
          return ResponseEntity.badRequest().body(e.getMessage());
      }

    }

    @GetMapping("/")
    @PreAuthorize("hasRole('CLIENT')")
    @Tag(name = "Cliente")
    @Operation(summary = "GET do Cliente")
    public ResponseEntity<Object> get(HttpServletRequest request) {
        var idClient = request.getAttribute("client_id");
        try{
            var profile = this.profileClientUseCase.execute(UUID.fromString(idClient.toString()));
            return ResponseEntity.ok(profile);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/services")
    @PreAuthorize("hasRole('CLIENT')")
    @Tag(name = "Cliente")
    @Operation(summary = "GET em todos os Serviços")
    public ResponseEntity<List<ServiceDTO>> getAllServices() {
        List<ServicesEntity> services = servicesRepository.findAll();
        List<ServiceDTO> dtos = services.stream().map(service -> {
            ServiceDTO dto = new ServiceDTO();
            dto.setId(service.getId());
            dto.setName(service.getName());
            dto.setPrice(service.getPrice());
            dto.setDescription(service.getDescription());
            dto.setDuration(service.getDuration());
            return dto;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

}
