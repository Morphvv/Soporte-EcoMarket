package com.SoporteMicroServicio.SoporteM.service;

import java.time.LocalDateTime;
import java.util.List;

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

    public List<ResolucionSoporte> listarTodos() {
        return resolucionSoporteRepository.findAll();
    }

    public ResolucionSoporte obtenerPorIdResolucion(Long idResolucion) {
        return resolucionSoporteRepository.findById(idResolucion)
            .orElseThrow(() -> new ResourceNotFoundException("Resolucion de soporte", idResolucion));
    }

    public ResolucionSoporte registrarResolucion(Long idTicket, ResolucionSoporteDTO dto) {
        log.info("Registrando resolucion para ticket: {}", idTicket);

        TicketSoporte ticket = ticketSoporteRepository.findById(idTicket)
            .orElseThrow(() -> new ResourceNotFoundException("Ticket", idTicket));

        if (resolucionSoporteRepository.findByTicketSoporteIdTicket(idTicket).isPresent()) {
            throw new BusinessException("El ticket ya tiene una resolucion registrada");
        }

        ResolucionSoporte resolucion = ResolucionSoporte.builder()
            .tipoResolucion(dto.getTipoResolucion())
            .descripcion(dto.getDescripcion())
            .aprobadoPor(dto.getAprobadoPor())
            .fechaResolucion(LocalDateTime.now())
            .ticketSoporte(ticket)
            .build();

        return resolucionSoporteRepository.save(resolucion);
    }

    public ResolucionSoporte modificarResolucion(Long idResolucion, ResolucionSoporteDTO dto) {
        log.info("Modificando resolucion {}", idResolucion);

        ResolucionSoporte r = obtenerPorIdResolucion(idResolucion);
        r.setTipoResolucion(dto.getTipoResolucion());
        r.setDescripcion(dto.getDescripcion());
        r.setAprobadoPor(dto.getAprobadoPor());
        return resolucionSoporteRepository.save(r);
    }

}
