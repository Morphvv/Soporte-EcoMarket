package com.SoporteMicroServicio.SoporteM.dto;

import org.antlr.v4.runtime.misc.NotNull;

import lombok.Data;

@Data
public class ReclamoDTO {

    @NotNull(message = "El id del pedido es obligatorio")
    @Positive 
    private Long idPedido;

    @NotNull(message = "El id del producto es obligatorio")
    @Positive 
    private Long idProducto;

    @NotBlank(message = "El motivo es obligatorio")
    @Size(min = 5, max = 200)
    private String motivo;

    @NotBlank(message = "La descripción es obligatoria")
    @Size(min = 10, max = 500)
    private String descripcion;
    
}
