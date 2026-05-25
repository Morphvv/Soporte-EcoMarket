package com.SoporteMicroServicio.SoporteM.dto;

import org.hibernate.annotations.processing.Pattern;

import lombok.Data;

@Data
public class PersonalSoporteDTO {

    @NotBlank(message = "El rut del personal es obligatoria")
    private Long idPersonalSoporte;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, max = 100)
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    @Size(min = 2, max = 100)
    private String apellido;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "Email con formato inválido")
    @Size(max = 150)
    private String email;

    @NotBlank(message = "El rol es obligatorio")
    @Pattern(regexp = "Agente/Supervisor/Administrador", message = "Rol invalido")
    private String rol;

    @NotBlank(message = "El estado es obligatorio")
    @Pattern(regexp = "Activo/Inactivo" , message = "Estado inválido")
    private String estado;
    
}
