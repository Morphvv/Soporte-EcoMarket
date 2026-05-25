package com.SoporteMicroServicio.SoporteM.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.SoporteMicroServicio.SoporteM.dto.CambiarEstadoDTO;
import com.SoporteMicroServicio.SoporteM.dto.CrearTicketDTO;
import com.SoporteMicroServicio.SoporteM.exception.BusinessException;
import com.SoporteMicroServicio.SoporteM.exception.ResourceNotFoundException;
import com.SoporteMicroServicio.SoporteM.feign.UsuarioFeignClient;
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

    //Consultas 

    public List<TicketSoporte> listarTodosLosTickets(){
        log.debug("Listando todos los tickets de soporte");
        return ticketSoporteRepository.findAll();
    }

    public TicketSoporte obtenerTicketPorId(Long idTicket){
        log.debug("Buscando ticket con id: {}", idTicket);
        return ticketSoporteRepository.findByIdTicket(idTicket)
            .orElseThrow(() -> new ResourceNotFoundException("Ticket no encontrado por su id"));
    }

    public List<TicketSoporte> listarPorCliente(Long runCliente){
        return ticketSoporteRepository.findByRunCliente(runCliente);
    }

    public List<TicketSoporte> listarPorEstado(String estado){
        return ticketSoporteRepository.findByEstado(estado);
    }

    //Crear ticket
    public TicketSoporte crearTicket(CrearTicketDTO dto){
        log.info("Creando ticker para cliente RUT: {}", dto.getRunCliente());

        //Validamos el usuario consultando al microservicio de usuario
        Map<String, Object> usuario = usuarioFeignClient.obtenerUsuarioPorRut(dto.getRunCliente());

        if("Usuario no disponible".equals(usuario.get(nombre))){
            log.error("No se pudo validar el cliente, rut: {}", dto.getRunCliente());
            throw new BusinessException("No se pudo crear el ticket: cliente no validado")
        }

        if ("Inactivo".equals(usuario.get("estadoUsuario"))){
            throw new BusinessException("No se pudo crear el ticket: cliente inactivo");
        {

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
            return ticketSoporteRepository.save(ticket);
        }

        TicketSoporte guardado = ticketRepository.save(ticket);

        registrarCambioEstado(guardado, null, "Abierto", "Sistema");

        log.info("Ticket creado: id: {}", guardado.getIdTicket());
        return guardado;
    }

    public TicketSoporte clasificarSolicitud(Long idTicket, String nuevaPrioridad, Long idPersonal){
        log.info("Clasificando ticket {} prioridad: {} personal {}", idTicket, nuevaPrioridad, idPersonal);

        TicketSoporte ticket = obtenerPorIdTicket(idTicket);

        if (nuevaPrioridad != null && personalRepository.findByIdPerosonal(idPersonal.orElseThrow(() -> new ResourceNotFoundException("Personal", idPersonal)));)
    }

    if("Inactivo".equals(personal.getEstadoPersonal())){
        throw new BusinessException("No se puede asignar ticket al personal inactivo ");

    }

    ticket.setPersonalAsignado(personal);
}

return ticketRepository.save(ticket);

public TicketSoporte cambiarEstado(Long idTicket, CambiarEstadoDTO dto) {
        log.info("Cambiando estado del ticket {} a {}", idTicket, dto.getNuevoEstado());

        TicketSoporte ticket = obtenerPorIdTicket(idTicket);
        String estadoAnterior = ticket.getEstadoTicket();
        String estadoNuevo = dto.getNuevoEstado();

        //No se puede cambiar el estado de un ticket si ya estado cerrado

        if("Cerrado".equals(estadoAnterior)){
            throw new BusinessException("No se puede cambiar el estado de un ticket ya cerrrado");
        }

        //No cambiar al mismo estado
        if(estadoAnterior.equals(estadoNuevo)){
            throw new BusinessException("El ticket ya se encuentra en ese estadp" + estadoNuevo);
        }

        ticket.setEstadoTicket(estadoNuevo);
        TicketSoporte guardado = ticketRepository.save(ticket);

        registrarCambioEstado(guardado, estadoAnterior, estadoNuevo, dto.getUsuarioResponsable());

        return guardado;
    }

 public TicketSoporte cerrarTicket(Long idTicket, String usuarioResponsable) {
        log.info("Cerrando ticket {}", idTicket);

        TicketSoporte ticket = obtenerPorId(idTicket);

        if ("Cerrado".equals(ticket.getEstadoTicket())) {
            throw new BusinessException("El ticket ya está cerrado.");
        }

        String estadoAnterior = ticket.getEstadoTicket();
        ticket.setEstadoTicket("Cerrado");
        ticket.setFechaCierre(LocalDateTime.now());

        TicketSoporte guardado = ticketRepository.save(ticket);
        registrarCambioEstado(guardado, estadoAnterior, "Cerrado", usuarioResponsable);

        log.info("Ticket {} cerrado exitosamente", idTicket);
        return guardado;
    }

    public void eliminarTicketSoporte(Long id) {
        log.warn("Eliminando ticket id: {}", id);
        if (!ticketRepository.existsById(id)) {
            throw new ResourceNotFoundException("Ticket", id);
        }
        ticketRepository.deleteByIdTicket(id);
    }


    private void registrarCambioEstado(TicketSoporte ticket, String anterior,
                                        String nuevo, String responsable) {
        HistorialEstadoTicket historial = HistorialEstadoTicket.builder()
                .estadoAnterior(anterior)
                .estadoNuevo(nuevo)
                .fechaCambio(LocalDateTime.now())
                .usuarioResponsable(responsable)
                .ticket(ticket)
                .build();
        historialRepository.save(historial);
        log.debug("Historial registrado: {} → {}", anterior, nuevo);
    }
}