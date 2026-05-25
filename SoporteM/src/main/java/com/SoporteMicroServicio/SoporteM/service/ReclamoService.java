package com.SoporteMicroServicio.SoporteM.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.SoporteMicroServicio.SoporteM.dto.ReclamoDTO;
import com.SoporteMicroServicio.SoporteM.exception.BusinessException;
import com.SoporteMicroServicio.SoporteM.exception.ResourceNotFoundException;
import com.SoporteMicroServicio.SoporteM.model.Reclamo;
import com.SoporteMicroServicio.SoporteM.model.TicketSoporte;
import com.SoporteMicroServicio.SoporteM.repository.ReclamoRepository;
import com.SoporteMicroServicio.SoporteM.repository.TicketSoporteRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor

public class ReclamoService {

    private final ReclamoRepository reclamoRepository;
    private final TicketSoporteRepository ticketSoporteRepository;

    public List<Reclamo> listarTodosReclamos(){
        return reclamoRepository.findAll();
    }

    public Reclamo obtenerPorIdReclamo(Long idReclamo){
        return reclamoRepository.findByIdReclamo(idReclamo).orElseThrow(() -> new ResourceNotFoundException("Reclamo no encontrado", idReclamo));
    }
    
    //Registrar reclamo asociado a un ticket
    public Reclamo registrarReclamo(Long idTicket, ReclamoDTO dto){
        log.info("Registrando reclamo para ticket: {}", idTicket);

        TicketSoporte ticket = ticketSoporteRepository.findByIdTicket(idTicket).orElseThrow(() -> new ResourceNotFoundException("Ticket no encontrado", idTicket));

        if(reclamoRepository.findByTicketIdTicket(idTicket).isPresent()){
            throw new BusinessException("El ticket ya tiene un reclamo registrado");
        }

        Reclamo reclamo = Reclamo.builder()
                .idPedido(dto.getIdPedido())
                .idProducto(dto.getIdProducto())
                .motivo(dto.getMotivo())
                .descripcion(dto.getDescripcion())
                .estadoReclamo("EN_REVISION")
                .fechaReclamo(LocalDateTime.now())
                .ticket(ticket)
                .build();

        return reclamoRepository.save(reclamo);
    }

    //Marcar el reclamo como revisado
    public Reclamo revisarReclamo(Integer idReclamo) {
        log.info("Revisando reclamo {}", idReclamo);
        Reclamo r = obtenerPorId(idReclamo);
        r.setEstadoReclamo("REVISADO");
        return reclamoRepository.save(r);
    }

    //Cambiar el estado del reclamo(Resuelto, rechazado)
    public Reclamo actualziarEstado(Long idReclamo, String nuevoEstado){
        log.info("Actualizando estado del reclamo {} a {}", idReclamo, nuevoEstado);

        if (!List.of("En_Revision", "Revisado", "Resuelto", "Rechazado").contains(nuevoEstado)){
            throw new BusinessException("Estado de reclamo invalido" + nuevoEstado);
        }

        Reclamo r = obtenerPorId(idReclamo);
        r.setEstadoReclamo(nuevoEstado);
        return reclamoRepository.save(r);
    }


}
