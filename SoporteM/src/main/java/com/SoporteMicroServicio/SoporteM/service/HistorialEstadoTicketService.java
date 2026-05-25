package com.SoporteMicroServicio.SoporteM.service;

import java.util.List;

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

    public List<HistorialEstadoTicket> listarPorIdTicket(Long idTicket) {
        log.debug("Listando historial del ticket {}", idTicket);
        return historialEstadoTicketRepository.findByTicketSoporteIdTicketOrderByFechaCambioAsc(idTicket);
    }

    public HistorialEstadoTicket obtenerHistorialPorId(Long id) {
        return historialEstadoTicketRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Historial", id));
    }

}
