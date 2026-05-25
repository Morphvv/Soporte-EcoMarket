package com.SoporteMicroServicio.SoporteM.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "ticket_soporte")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class TicketSoporte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idTicket;

    @Column(name = "run_cliente", nullable = false)
    private Long runCliente;

    @Column(name = "id_pedido")
    private Long idPedido;

    @Column(nullable = false, length = 150)
    private String asunto;

    @Column(nullable = false, length = 500)
    private String descripcion;

    @Column(name = "tipo_solicitud", nullable = false, length = 50)
    private String tipoSolicitud;

    @Column(nullable = false, length = 30)
    private String canal;

    @Column(nullable = false, length = 20)
    private String prioridad;

    @Column(name = "estado_ticket", nullable = false, length = 30)
    private String estadoTicket;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_cierre")
    private LocalDateTime fechaCierre;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "personal_id")
    @ToString.Exclude
    private PersonalSoporte personalAsignado;

    @OneToMany(mappedBy = "ticketSoporte", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @ToString.Exclude
    private List<MensajeSoporte> mensajes = new ArrayList<>();

    @OneToMany(mappedBy = "ticketSoporte", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @ToString.Exclude
    private List<HistorialEstadoTicket> historial = new ArrayList<>();

    @OneToMany(mappedBy = "ticketSoporte", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @ToString.Exclude
    private List<EvidenciaAdjunta> evidencias = new ArrayList<>();

    @OneToOne(mappedBy = "ticketSoporte", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private SolicitudDevolucion solicitudDevolucion;

    @OneToOne(mappedBy = "ticketSoporte", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private ResolucionSoporte resolucionSoporte;

    @OneToOne(mappedBy = "ticketSoporte", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private Reclamo reclamo;

}
