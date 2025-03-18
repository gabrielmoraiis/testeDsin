package com.leila.salaoBeleza.modules.admin.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateServiceDTO {
    private String name;
    private String description;
    private BigDecimal price;
    private String duration;
}
