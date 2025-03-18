package com.leila.salaoBeleza.modules.admin.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

import java.math.BigDecimal;
import java.util.UUID;

@Entity(name = "tb_services")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ServicesEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "admin_id", nullable = false)
    private UUID adminId;

    @ManyToOne()
    @JoinColumn(name = "admin_id", insertable = false, updatable = false)
    private AdminEntity adminEntity;

    @NotBlank
    private String name;

    @NotBlank
    private String description;

    @NotNull(message = "O campo preço é obrigatório.")
    @DecimalMin(value = "0.0", inclusive = false, message = "O preço deve ser maior que zero.")
    private BigDecimal price;

    @NotBlank
    private String duration;
}
