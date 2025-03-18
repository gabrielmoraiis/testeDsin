package com.leila.salaoBeleza.modules.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthAdminDTO {

    private String email;
    private String password;
}
