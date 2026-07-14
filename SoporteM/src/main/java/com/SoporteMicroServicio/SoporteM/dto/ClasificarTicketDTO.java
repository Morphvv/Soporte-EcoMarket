package com.SoporteMicroServicio.SoporteM.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class ClasificarTicketDTO {

    @Pattern(regexp = "BAJA|MEDIA|ALTA|CRITICA", message = "Prioridad invalida")
    private String prioridad;

    @Positive(message = "El id del personal debe ser positivo")
    private Long idPersonal;

}
