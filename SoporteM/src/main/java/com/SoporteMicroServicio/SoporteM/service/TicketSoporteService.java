package com.SoporteMicroServicio.SoporteM.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.SoporteMicroServicio.SoporteM.dto.CambiarEstadoDTO;
import com.SoporteMicroServicio.SoporteM.dto.CrearTicketDTO;
import com.SoporteMicroServicio.SoporteM.exception.BusinessException;
import com.SoporteMicroServicio.SoporteM.exception.ResourceNotFoundException;
import com.SoporteMicroServicio.SoporteM.feign.PedidoFeignClient;
import com.SoporteMicroServicio.SoporteM.feign.UsuarioFeignClient;
import com.SoporteMicroServicio.SoporteM.model.HistorialEstadoTicket;
import com.SoporteMicroServicio.SoporteM.model.PersonalSoporte;
import com.SoporteMicroServicio.SoporteM.model.TicketSoporte;
import com.SoporteMicroServicio.SoporteM.repository.HistorialEstadoTicketRepository;
import com.SoporteMicroServicio.SoporteM.repository.PersonalSoporteRepository;
import com.SoporteMicroServicio.SoporteM.repository.TicketSoporteRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor

public class TicketSoporteService {

    private final TicketSoporteRepository ticketSoporteRepository;
    private final HistorialEstadoTicketRepository historialEstadoTicketRepository;
    private final PersonalSoporteRepository personalSoporteRepository;
    private final UsuarioFeignClient usuarioFeignClient;
    private final PedidoFeignClient pedidoFeignClient;

    public List<TicketSoporte> listarTodosLosTickets() {
        log.debug("Listando todos los tickets de soporte");
        return ticketSoporteRepository.findAll();
    }

    public TicketSoporte obtenerTicketPorId(Long idTicket) {
        log.debug("Buscando ticket con id: {}", idTicket);
        return ticketSoporteRepository.findById(idTicket)
            .orElseThrow(() -> new ResourceNotFoundException("Ticket", idTicket));
    }

    public List<TicketSoporte> listarPorCliente(Long runCliente) {
        return ticketSoporteRepository.findByRunCliente(runCliente);
    }

    public List<TicketSoporte> listarPorEstado(String estado) {
        return ticketSoporteRepository.findByEstadoTicket(estado);
    }

    public TicketSoporte crearTicket(CrearTicketDTO dto) {
        log.info("Creando ticket para cliente RUT: {}", dto.getRunCliente());

        Map<String, Object> usuario = usuarioFeignClient.obtenerUsuarioPorRut(dto.getRunCliente());

        if ("Usuario no disponible".equals(usuario.get("nombre"))) {
            log.error("No se pudo validar el cliente, rut: {}", dto.getRunCliente());
            throw new BusinessException("No se pudo crear el ticket: cliente no validado");
        }

        if ("Inactivo".equals(usuario.get("estadoUsuario"))) {
            throw new BusinessException("No se pudo crear el ticket: cliente inactivo");
        }

        if (dto.getIdPedido() != null) {
            Map<String, Object> pedido = pedidoFeignClient.obtenerPedidoPorId(dto.getIdPedido());
            if ("DESCONOCIDO".equals(pedido.get("estado"))) {
                log.error("No se pudo validar el pedido, id: {}", dto.getIdPedido());
                throw new BusinessException("No se pudo crear el ticket: pedido no validado");
            }
        }

        TicketSoporte ticket = TicketSoporte.builder()
            .runCliente(dto.getRunCliente())
            .idPedido(dto.getIdPedido())
            .asunto(dto.getAsunto())
            .descripcion(dto.getDescripcion())
            .tipoSolicitud(dto.getTipoSolicitud())
            .canal(dto.getCanal())
            .prioridad(dto.getPrioridad())
            .estadoTicket("ABIERTO")
            .fechaCreacion(LocalDateTime.now())
            .build();

        TicketSoporte guardado = ticketSoporteRepository.save(ticket);
        registrarCambioEstado(guardado, null, "ABIERTO", "Sistema");
        log.info("Ticket creado id: {}", guardado.getIdTicket());
        return guardado;
    }

    public TicketSoporte clasificarSolicitud(Long idTicket, String nuevaPrioridad, Long idPersonal) {
        log.info("Clasificando ticket {} prioridad: {} personal {}", idTicket, nuevaPrioridad, idPersonal);

        TicketSoporte ticket = obtenerTicketPorId(idTicket);

        if (nuevaPrioridad != null) {
            ticket.setPrioridad(nuevaPrioridad);
        }

        if (idPersonal != null) {
            PersonalSoporte personal = personalSoporteRepository.findById(idPersonal)
                .orElseThrow(() -> new ResourceNotFoundException("Personal", idPersonal));

            if ("INACTIVO".equals(personal.getEstado())) {
                throw new BusinessException("No se puede asignar ticket al personal inactivo");
            }

            ticket.setPersonalAsignado(personal);
        }

        return ticketSoporteRepository.save(ticket);
    }

    public TicketSoporte cambiarEstado(Long idTicket, CambiarEstadoDTO dto) {
        log.info("Cambiando estado del ticket {} a {}", idTicket, dto.getNuevoEstado());

        TicketSoporte ticket = obtenerTicketPorId(idTicket);
        String estadoAnterior = ticket.getEstadoTicket();
        String estadoNuevo = dto.getNuevoEstado();

        if ("CERRADO".equals(estadoAnterior)) {
            throw new BusinessException("No se puede cambiar el estado de un ticket ya cerrado");
        }

        if (estadoAnterior.equals(estadoNuevo)) {
            throw new BusinessException("El ticket ya se encuentra en ese estado: " + estadoNuevo);
        }

        ticket.setEstadoTicket(estadoNuevo);
        TicketSoporte guardado = ticketSoporteRepository.save(ticket);
        registrarCambioEstado(guardado, estadoAnterior, estadoNuevo, dto.getUsuarioResponsable());
        return guardado;
    }

    public TicketSoporte cerrarTicket(Long idTicket, String usuarioResponsable) {
        log.info("Cerrando ticket {}", idTicket);

        TicketSoporte ticket = obtenerTicketPorId(idTicket);

        if ("CERRADO".equals(ticket.getEstadoTicket())) {
            throw new BusinessException("El ticket ya esta cerrado.");
        }

        String estadoAnterior = ticket.getEstadoTicket();
        ticket.setEstadoTicket("CERRADO");
        ticket.setFechaCierre(LocalDateTime.now());

        TicketSoporte guardado = ticketSoporteRepository.save(ticket);
        registrarCambioEstado(guardado, estadoAnterior, "CERRADO", usuarioResponsable);
        log.info("Ticket {} cerrado exitosamente", idTicket);
        return guardado;
    }

    public void eliminarTicketSoporte(Long id) {
        log.warn("Eliminando ticket id: {}", id);
        if (!ticketSoporteRepository.existsById(id)) {
            throw new ResourceNotFoundException("Ticket", id);
        }
        ticketSoporteRepository.deleteById(id);
    }

    private void registrarCambioEstado(TicketSoporte ticket, String anterior, String nuevo, String responsable) {
        HistorialEstadoTicket historial = HistorialEstadoTicket.builder()
            .estadoAnterior(anterior)
            .estadoNuevo(nuevo)
            .fechaCambio(LocalDateTime.now())
            .usuarioResponsable(responsable)
            .ticketSoporte(ticket)
            .build();
        historialEstadoTicketRepository.save(historial);
        log.debug("Historial registrado: {} -> {}", anterior, nuevo);
    }

}
