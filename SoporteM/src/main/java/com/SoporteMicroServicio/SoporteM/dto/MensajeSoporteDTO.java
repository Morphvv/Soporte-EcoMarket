package com.SoporteMicroServicio.SoporteM.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class MensajeSoporteDTO {

    @NotBlank(message = "El contenido del mensaje es obligatorio")
    @Size(min = 1, max = 1000)
    private String contenido;

    @NotBlank(message = "El remitente es obligatorio")
    @Size(max = 100)
    private String remitente;

    @NotBlank(message = "El tipo de remitente es obligatorio")
    @Pattern(regexp = "CLIENTE|PERSONAL_SOPORTE|SISTEMA", message = "Tipo de remitente invalido")
    private String tipoRemitente;

}
