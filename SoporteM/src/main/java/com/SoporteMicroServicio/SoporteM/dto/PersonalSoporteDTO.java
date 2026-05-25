package com.SoporteMicroServicio.SoporteM.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PersonalSoporteDTO {

    @NotNull(message = "El rut del personal es obligatorio")
    private Long rut;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, max = 100)
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    @Size(min = 2, max = 100)
    private String apellido;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "Email con formato invalido")
    @Size(max = 150)
    private String email;

    @NotBlank(message = "El rol es obligatorio")
    @Pattern(regexp = "AGENTE|SUPERVISOR|ADMINISTRADOR", message = "Rol invalido")
    private String rol;

    @NotBlank(message = "El estado es obligatorio")
    @Pattern(regexp = "ACTIVO|INACTIVO", message = "Estado invalido")
    private String estado;

}
