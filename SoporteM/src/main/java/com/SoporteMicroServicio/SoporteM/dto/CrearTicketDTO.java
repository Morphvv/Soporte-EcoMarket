package com.SoporteMicroServicio.SoporteM.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CrearTicketDTO {

    @NotNull(message = "El RUT del cliente es obligatorio")
    @Positive(message = "El rut debe ser positivo")
    private Long runCliente;

    private Long idPedido;

    @NotBlank(message = "El asunto es obligatorio")
    @Size(min = 5, max = 150, message = "El asunto debe tener entre 5 a 150 caracteres")
    private String asunto;

    @NotBlank(message = "La descripcion es obligatoria")
    @Size(min = 10, max = 500, message = "La descripcion es de 10 a 500 caracteres")
    private String descripcion;

    @NotBlank(message = "El tipo de solicitud es obligatoria")
    @Pattern(regexp = "CONSULTA|RECLAMO|DEVOLUCION|SOPORTE_TECNICO", message = "Tipo de solicitud invalido")
    private String tipoSolicitud;

    @NotBlank(message = "El canal es obligatorio")
    @Pattern(regexp = "WEB|EMAIL|TELEFONO|CHAT", message = "Canal invalido")
    private String canal;

    @NotBlank(message = "La prioridad es obligatoria")
    @Pattern(regexp = "BAJA|MEDIA|ALTA|CRITICA", message = "Prioridad invalida")
    private String prioridad;

}
