package com.SoporteMicroServicio.SoporteM.dto;

import org.antlr.v4.runtime.misc.NotNull;

import lombok.Data;

@Data
public class SolicitudDevolucionDTO {

    @NotNull(message = "El id del pedido es obligatorio")
    @Positive
    private Long idPedido;

    @NotNull(message = "El id del producto es obligatorio")
    @Positive 
    private Long idProducto;

    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "La cantidad minima es 1")
    private Integer cantidad;

    @NotBlank(message = "El motivo es obligatorio")
    @Size(min = 5, max = 300)
    private String motivo;
    
}
