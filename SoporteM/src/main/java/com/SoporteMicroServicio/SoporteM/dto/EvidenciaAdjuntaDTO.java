package com.SoporteMicroServicio.SoporteM.dto;

import org.hibernate.annotations.processing.Pattern;

import lombok.Data;

@Data
public class EvidenciaAdjuntaDTO {

    @NotBlank(message = "El nombre del archivo es obligatorio")
    @Size (max = 200)
    private String nombreArchivo;

    @NotBlank(message = "El tipo de archivo es obligatorio")
    @Pattern(regexp = "Imagen/PDF/Video/Documento", message = "El tipo de archivo invalido")
    private String tipoArchivo;

    @NotBlank(message = "La URL del archivo es obligatoria")
    @Size(max = 500)
    private String urlArchivo;
    
}
