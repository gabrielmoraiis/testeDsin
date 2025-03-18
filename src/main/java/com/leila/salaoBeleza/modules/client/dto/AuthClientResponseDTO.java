package com.leila.salaoBeleza.modules.client.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthClientResponseDTO {

    private String accessToken;
    private Long expires_in;
    private List<String> roles;
}
