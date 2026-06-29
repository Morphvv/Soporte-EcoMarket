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
@Table(name = "resolucion_soporte")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties({"ticketSoporte", "hibernateLazyInitializer", "handler"})
public class ResolucionSoporte {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long idResolucion;

    @Column(name = "tipo_resolucion", nullable = false, length = 50)
    private String tipoResolucion;

    @Column(nullable= false, length= 500)
    private String descripcion;

    @Column(name = "aprobado_por", nullable= false, length= 100)
    private String aprobadoPor;

    @Column(name = "fecha_resolucion", nullable= false)
    private LocalDateTime fechaResolucion;

    @OneToOne(fetch= FetchType.LAZY)
    @JoinColumn(name = "id_ticket", nullable= false, unique= true)
    @ToString.Exclude
    private TicketSoporte ticketSoporte;

}
