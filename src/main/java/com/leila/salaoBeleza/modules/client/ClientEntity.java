package com.leila.salaoBeleza.modules.client;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity(name = "tb_client")
public class ClientEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank
    private String name;

    @Email(message = "O campo (email) deve conter um e-mail válido.")
    private String email;

    @Length(min = 6, max= 100, message = "A senha deve conter entre (6) e (100) caracteres.")
    private String password;

    @NotBlank
    @Pattern(
            regexp = "^\\([1-9]{2}\\) (?:[2-8]|9[0-9])[0-9]{3}\\-[0-9]{4}$",
            message = "Número de celular inválido. (xx) xxxx-xxxx"
    )
    private String cellPhoneNumber;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
