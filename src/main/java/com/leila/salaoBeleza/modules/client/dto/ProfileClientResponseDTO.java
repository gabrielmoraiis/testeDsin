package com.leila.salaoBeleza.modules.client.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProfileClientResponseDTO {

    private String name;
    private String email;
    private String cellPhoneNumber;
    private UUID id;
}
