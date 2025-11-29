package com.desarrollox.backend.api_register.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Past;
import java.time.LocalDate;
import jakarta.validation.constraints.Pattern;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, max = 50, message = "El nombre no cumple con el tamaño deseado")
    private String name;

    @Column(name = "email", nullable = false, unique = true)
    @Email(message = "Email no valido")
    @Size(min = 5, max = 100, message = "Email debe estar entre 5 y 100 caracteres")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]{6,}@gmail\\.com$", message = "Email debe ser una dirección de email válida con almenos 6 caracteres antes de @gmail.com")
    private String email;

    @Column(name = "password", nullable = false)
    @Size(min = 8, max = 128, message = "La contraseña debe de estar entre 8 y 128 caracteres")
    @Pattern(
        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&]).{8,128}$",
        message = "La contraseña debe contener al menos una letra minúscula, una mayúscula, un dígito y un caracter especial"
    )
    private String password;

    @Column(name = "birthdate", nullable = false)
    @Past(message = "La fecha de nacimiento debe de ser una fecha pasada")
    private LocalDate birthdate;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;

    public enum Role {
        CLIENTE,
        ADMIN
    }
}