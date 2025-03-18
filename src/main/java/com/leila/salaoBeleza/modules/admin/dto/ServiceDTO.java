package com.leila.salaoBeleza.modules.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceDTO {
    private UUID id;
    private String name;
    private BigDecimal price;
    private String description;
    private String duration;
}
