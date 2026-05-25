package com.SoporteMicroServicio.SoporteM.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.SoporteMicroServicio.SoporteM.dto.ResolucionSoporteDTO;
import com.SoporteMicroServicio.SoporteM.exception.BusinessException;
import com.SoporteMicroServicio.SoporteM.exception.ResourceNotFoundException;
import com.SoporteMicroServicio.SoporteM.model.ResolucionSoporte;
import com.SoporteMicroServicio.SoporteM.model.TicketSoporte;
import com.SoporteMicroServicio.SoporteM.repository.ResolucionSoporteRepository;
import com.SoporteMicroServicio.SoporteM.repository.TicketSoporteRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor

public class ResolucionSoporteService {

    private final ResolucionSoporteRepository resolucionSoporteRepository;
    private final TicketSoporteRepository ticketSoporteRepository;

    public List<ResolucionSoporte> listarTodos(){
        return resolucionSoporteRepository.findAll();
    }

    public ResolucionSoporte obtenerPorIdResolucion(Long idResolucion){
        return resolucionSoporteRepository.findByIdResolucion(idResolucion).orElseThrow() -> new ResourceNotFoundException("Resolucion de soporte no encontrado", idResolucion);
    }

    //Registrar resolucion final de un ticket
    public ResolucionSoporte registrarResolucion(Long idTicket, ResolucionSoporteDTO dto){
        log.info("Registrando resolucion para ticket: {}", idTicket);

        TicketSoporte ticket = ticketSoporteRepository.findByIdTicket(idTicket).orElseThrow(() -> new ResourceNotFoundException("Ticket no encontrado", idTicket));

        if (resolucionSoporteRepository.findByTicketIdTicket(idTicket).isPresent()){
            throw new BusinessException("El ticket ya tiene una resolucion registrada");
        }

        ResolucionSoporte resolucion = ResolucionSoporte.builder()
                .tipoResolucion(dto.getTipoResolucion())
                .descripcion(dto.getDescripcion())
                .aprobadoPor(dto.getAprobadoPor())
                .fechaResolucion(LocalDateTime.now())
                .ticket(ticket)
                .build();

            return resolucionSoporteRepository.save(resolucion);
    }

    //Modificar la descripcion y tipo
    public ResolucionSoporte modificarResolucion(Integer idResolucion, ResolucionSoporteDTO dto) {
        log.info("Modificando resolución {}", idResolucion);

        ResolucionSoporte r = obtenerPorId(idResolucion);
        r.setTipoResolucion(dto.getTipoResolucion());
        r.setDescripcion(dto.getDescripcion());
        r.setAprobadoPor(dto.getAprobadoPor());
        return resolucionRepository.save(r);
    }
    
}
