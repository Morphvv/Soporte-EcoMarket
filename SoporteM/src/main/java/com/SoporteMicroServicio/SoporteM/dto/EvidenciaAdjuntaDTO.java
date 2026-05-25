package com.SoporteMicroServicio.SoporteM.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class EvidenciaAdjuntaDTO {

    @NotBlank(message = "El nombre del archivo es obligatorio")
    @Size(max = 200)
    private String nombreArchivo;

    @NotBlank(message = "El tipo de archivo es obligatorio")
    @Pattern(regexp = "IMAGEN|PDF|VIDEO|DOCUMENTO", message = "El tipo de archivo invalido")
    private String tipoArchivo;

    @NotBlank(message = "La URL del archivo es obligatoria")
    @Size(max = 500)
    private String urlArchivo;

}
