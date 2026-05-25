package com.SoporteMicroServicio.SoporteM.service;

import org.springframework.stereotype.Service;

import com.SoporteMicroServicio.SoporteM.exception.ResourceNotFoundException;
import com.SoporteMicroServicio.SoporteM.model.HistorialEstadoTicket;
import com.SoporteMicroServicio.SoporteM.repository.HistorialEstadoTicketRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor

public class HistorialEstadoTicketService {

    private final HistorialEstadoTicketRepository historialEstadoTicketRepository;

    public final HistorialEstadoTicket listarPorIdTicket(Long idTicket){
        log.debug("Listadno historial del ticket {}", idTicket);
        return historialEstadoTicketRepository.findByIdTicket(idTicket).orElseThrow(() -> new ResourveNotFoundException("Historial de estado del ticket no encontrado", idTicket));
    }

    public HistorialEstadoTicket obtenerPorId(Integer id) {
        return historialRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Historial", id));
    }
    
}
