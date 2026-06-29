package com.SoporteMicroServicio.SoporteM.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "solicitud_devolucion")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties({"ticketSoporte", "hibernateLazyInitializer", "handler"})
public class SolicitudDevolucion {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long idSolicitudD;

    @Column(name = "id_pedido", nullable = false)
    private Long idPedido;

    @Column(name = "id_producto", nullable = false)
    private Long idProducto;

    @Column(nullable= false)
    private Integer cantidad;

    @Column(nullable = false, length=300)
    private String motivo;

    @Column(name = "estado_solicitud", nullable= false, length= 30)
    private String estadoSolicitud;

    @Column(name = "fecha_solicitud", nullable= false)
    private LocalDateTime fechaSolicitud;

    @OneToOne(fetch= FetchType.LAZY)
    @JoinColumn(name = "ticket_id", nullable= false, unique = true)
    @ToString.Exclude
    private TicketSoporte ticketSoporte;

}
