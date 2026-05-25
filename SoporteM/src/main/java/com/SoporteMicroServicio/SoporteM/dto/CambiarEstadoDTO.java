package com.SoporteMicroServicio.SoporteM.dto;

import lombok.Data;

@Data
public class CambiarEstadoDTO {

    @NotBlank(message = "El nuevo estado es obligatorio")
    @Pattern(regexp = "ABIERTO/EN_PROCESO/RESUELTO/CERRADO/CANCELADO", message = "Estado invalido")
    private String nuevoEstado;

    @NotBlank(message = "El usuario responsable es obligatorio")
    private String usuarioResponsable;
    
}
