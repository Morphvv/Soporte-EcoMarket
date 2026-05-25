package com.SoporteMicroServicio.SoporteM.service;

import java.time.LocalDateTime;

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

    public List<SolicitudDevolucion> listarTodos(){
        return solicitudDevolucionRepository.findAll();
    }

    public SolicitudDevolucion obtenerPorIdDevolucion(Long idDevolucion){
        return solicitudDevolucionRepository.findByIdDevolucion(idDevolucion).orElseThrow(() -> new ResourceNotFoundException("Solicitud de devolucion no encontrado"));
    }

    //Crear solicitud de devolucion asociado a un ticket 
    public SolicitudDevolucion registrarSolicitud(Long idTicket, SolicitudDevolucionDTO dto){
        log.info("Registrando solicitud de devolucion para ticket: {}", idTicket);

        TicketSoporte ticket = ticketSoporteRepository.findByIdTicket(idTicket).orElseThrow(() -> new ResourceNotFoundException("Ticket no encontrado", idTicket));
        
        if(solicitudRepository.findByTicketIdTicket(idTicket).isPresent()){
            throw new BusinessException("El ticket ya tiene una solicitud de devolucion registrada");
        }

        SolicitudDevolucion solicitud = SolicitudDevolucion.builder()
                .idPedido(dto.getIdPedido())
                .idProducto(dto.getIdProducto())
                .cantidad(dto.getCantidad())
                .motivo(dto.getMotivo())
                .estadoSolicitud("PENDIENTE")
                .fechaSolicitud(LocalDateTime.now())
                .ticket(ticket)
                .build();

        return solicitudRepository.save(solicitud);
    }

    //Validar datos de la solicitud
    public Boolean validarProducto(Long idSolicitud){
        SolicitudDevolucion solicitud = obtenerPorIdDevolucion(idSolicitud){
                SolicitudDevolucion s = obtenerPorId(idSolicitud);
                boolean valido = s.getIdProducto() != null
                      && s.getCantidad() != null
                      && s.getCantidad() > 0;
        log.info("Validación de producto en solicitud {}: {}", idSolicitud, valido);
        return valido;

        }
    }

    //Aprobar solicitud
    public SolicitudDevolucion aprobarSolicitud(Long idSolicitud){
        log.info("Aprobando solicitud de devolucion id: {}", idSolicitud);

        SolicitudDevolucion solicitud = obtenerPorIdDevolucion(idSolicitud);
        if (!validarProducto(idSolicitud)){
            throw new BusinessException("No se puede aprobar la solicitud: datos del producto no válidos");
        }

        solicitud.setEstadoSolicitud("APROBADA");
        return solicitudRepository.save(solicitud);
    }

    //Rechazar solicitud
    public SolicitudDevolucion rechazarDevolucion(Integer idSolicitud) {
        log.info("Rechazando devolución {}", idSolicitud);
        SolicitudDevolucion s = obtenerPorIdSolicitud(idSolicitud);

        if (!"PENDIENTE".equals(s.getEstadoSolicitud())) {
            throw new BusinessException(
                "Solo se pueden rechazar solicitudes en estado PENDIENTE.");
        }
        s.setEstadoSolicitud("RECHAZADA");
        return solicitudRepository.save(s);
    }
    
    
}
