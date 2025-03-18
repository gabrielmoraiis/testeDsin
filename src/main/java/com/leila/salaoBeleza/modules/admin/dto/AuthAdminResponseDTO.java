package com.leila.salaoBeleza.modules.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthAdminResponseDTO {

    private String access_token;
    private Long expires_in;
}
