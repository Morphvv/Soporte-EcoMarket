package com.SoporteMicroServicio.SoporteM.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ResolucionSoporteDTO {

    @NotBlank(message = "El tipo de resolucion es obligatorio")
    @Pattern(regexp = "REEMBOLSO|REEMPLAZO|DEVOLUCION|RECHAZO", message = "Tipo de resolucion invalido")
    private String tipoResolucion;

    @NotBlank(message = "La descripcion es obligatoria")
    @Size(min = 10, max = 500)
    private String descripcion;

    @NotBlank(message = "Debe indicar quien aprobo la resolucion")
    @Size(max = 100)
    private String aprobadoPor;

}
