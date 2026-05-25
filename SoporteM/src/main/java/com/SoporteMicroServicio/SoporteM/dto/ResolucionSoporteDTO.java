package com.SoporteMicroServicio.SoporteM.dto;

import org.hibernate.annotations.processing.Pattern;

import lombok.Data;

@Data
public class ResolucionSoporteDTO {

    @NotBlank(message = "El tipo de resolucion es obligatorio")
    @Pattern(regeexp = "Reembolso/Reemplazo/Devolucion/Rechazo", message = "Tipo de resolucion invalido")
    private String tipoResolucion;

    @NotBlank(message = "La descripcion es obligatoria")
    @Size (min = 10, max = 500)
    private String descripcion;
    
    @NotBlank(message = "Debe indicar quien aprobo la resolucion")
    @Size (max = 100)
    private String aprobadoPor;
    
}
