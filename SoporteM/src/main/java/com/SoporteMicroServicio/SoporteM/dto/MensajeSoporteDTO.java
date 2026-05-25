package com.SoporteMicroServicio.SoporteM.dto;

import org.hibernate.annotations.processing.Pattern;

import lombok.Data;

@Data
public class MensajeSoporteDTO {

    @NotBlank(message = "El contenido del mensaje es obligatorio")
    @Size(min = 1, max = 1000)
    private String contenido;

    @NotBlank(message = "El remitente es obligatorio")
    @Size  (max =1000)
    private String remitente;

    @NotBlank(message = "El tipo de remintente es obligatorio")
    @Pattern(regexp = "CLIENTE/PERSONAL_SOPORTE/SISTEMA", message = "Tipo de remitente invalido")
    private String tipoRemitente;
    
}
