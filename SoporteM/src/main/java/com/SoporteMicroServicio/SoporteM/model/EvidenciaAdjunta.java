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
@Table(name = "evidencia_adjunta")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class EvidenciaAdjunta {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long idEvidencia;

    @Column(name = "nombre_archivo", nullable = false, length = 200)
    private String nombreArchivo;

    @Column(name = "tipo_archivo", nullable= false, length=50)
    private String tipoArchivo;

    @Column(name = "url_archivo", nullable= false, length= 500)
    private String urlArchivo;

    @Column(name = "fecha_carga", nullable= false)
    private LocalDateTime fechaCarga;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_ticket", nullable= false)
    @ToString.Exclude
    private TicketSoporte ticketSoporte;
}
