package com.SoporteMicroServicio.SoporteM.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.SoporteMicroServicio.SoporteM.dto.SolicitudDevolucionDTO;
import com.SoporteMicroServicio.SoporteM.exception.BusinessException;
import com.SoporteMicroServicio.SoporteM.exception.ResourceNotFoundException;
import com.SoporteMicroServicio.SoporteM.model.SolicitudDevolucion;
import com.SoporteMicroServicio.SoporteM.model.TicketSoporte;
import com.SoporteMicroServicio.SoporteM.repository.SolicitudDevolucionRepository;
import com.SoporteMicroServicio.SoporteM.repository.TicketSoporteRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor

public class SolicitudDevolucionService {

    private final SolicitudDevolucionRepository solicitudDevolucionRepository;
    private final TicketSoporteRepository ticketSoporteRepository;

    public List<SolicitudDevolucion> listarTodos() {
        return solicitudDevolucionRepository.findAll();
    }

    public SolicitudDevolucion obtenerPorIdDevolucion(Long idDevolucion) {
        return solicitudDevolucionRepository.findById(idDevolucion)
            .orElseThrow(() -> new ResourceNotFoundException("Solicitud de devolucion", idDevolucion));
    }

    public SolicitudDevolucion registrarSolicitud(Long idTicket, SolicitudDevolucionDTO dto) {
        log.info("Registrando solicitud de devolucion para ticket: {}", idTicket);

        TicketSoporte ticket = ticketSoporteRepository.findById(idTicket)
            .orElseThrow(() -> new ResourceNotFoundException("Ticket", idTicket));

        if (solicitudDevolucionRepository.findByTicketSoporteIdTicket(idTicket).isPresent()) {
            throw new BusinessException("El ticket ya tiene una solicitud de devolucion registrada");
        }

        SolicitudDevolucion solicitud = SolicitudDevolucion.builder()
            .idPedido(dto.getIdPedido())
            .idProducto(dto.getIdProducto())
            .cantidad(dto.getCantidad())
            .motivo(dto.getMotivo())
            .estadoSolicitud("PENDIENTE")
            .fechaSolicitud(LocalDateTime.now())
            .ticketSoporte(ticket)
            .build();

        return solicitudDevolucionRepository.save(solicitud);
    }

    public Boolean validarProducto(Long idSolicitud) {
        SolicitudDevolucion solicitud = obtenerPorIdDevolucion(idSolicitud);
        boolean valido = solicitud.getIdProducto() != null
            && solicitud.getCantidad() != null
            && solicitud.getCantidad() > 0;
        log.info("Validacion de producto en solicitud {}: {}", idSolicitud, valido);
        return valido;
    }

    public SolicitudDevolucion aprobarSolicitud(Long idSolicitud) {
        log.info("Aprobando solicitud de devolucion id: {}", idSolicitud);

        SolicitudDevolucion solicitud = obtenerPorIdDevolucion(idSolicitud);
        if (!validarProducto(idSolicitud)) {
            throw new BusinessException("No se puede aprobar la solicitud: datos del producto no validos");
        }

        solicitud.setEstadoSolicitud("APROBADA");
        return solicitudDevolucionRepository.save(solicitud);
    }

    public SolicitudDevolucion rechazarDevolucion(Long idSolicitud) {
        log.info("Rechazando devolucion {}", idSolicitud);
        SolicitudDevolucion s = obtenerPorIdDevolucion(idSolicitud);

        if (!"PENDIENTE".equals(s.getEstadoSolicitud())) {
            throw new BusinessException("Solo se pueden rechazar solicitudes en estado PENDIENTE.");
        }
        s.setEstadoSolicitud("RECHAZADA");
        return solicitudDevolucionRepository.save(s);
    }

}
