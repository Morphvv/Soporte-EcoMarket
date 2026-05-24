package com.SoporteMicroServicio.SoporteM.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table (name = "hisorial_estado_ticket")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class HistorialEstadoTicket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idHistorial;

    @Column(name = "estado_anterior", length = 30)
    private String estadoAnterior;

    @Column(name = "estado_nuevo", nullable= false, length= 30)
    private String estadoNuevo;

    @Column(name = "fecha_cambio", nullable= false)
    private LocalDateTime fechaCambio;

    @Column(name = "usuario_responsable", nullable= false, length= 100)
    private String usuarioResponsable;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_ticket", nullable= false)
    @ToString.Exclude
    private TicketSoporte ticketSoporte;
    
}
