package com.SoporteMicroServicio.SoporteM.model;

import java.time.LocalDateTime;

import jakarta.annotation.Generated;
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
@Table(name = "mensaje_soporte")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class MensajeSoporte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idMensaje;

    @Column(nullable = false, length = 1000)
    private String contenido;

    @Column(nullable = false, length= 100)
    private String remitente;

    @Column(name = "tipo_remitente", nullable= = false, length = 30)
    private String tipoRemitente;

    @Column(name = "fecha_envio", nullable= false)
    private LocalDateTime fechaEnvio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_id", nullable= false)
    @ToString.Exclude
    private TicketSoporte ticketSoporte;
    
}
