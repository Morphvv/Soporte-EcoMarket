package com.SoporteMicroServicio.SoporteM.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.SoporteMicroServicio.SoporteM.dto.EvidenciaAdjuntaDTO;
import com.SoporteMicroServicio.SoporteM.exception.ResourceNotFoundException;
import com.SoporteMicroServicio.SoporteM.model.EvidenciaAdjunta;
import com.SoporteMicroServicio.SoporteM.model.TicketSoporte;
import com.SoporteMicroServicio.SoporteM.repository.EvidenciaAdjuntaRepository;
import com.SoporteMicroServicio.SoporteM.repository.TicketSoporteRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor

public class EvidenciaAdjuntaService {

    private final EvidenciaAdjuntaRepository evidenciaAdjuntaRepository;
    private final TicketSoporteRepository ticketSoporteRepository;

    public List<EvidenciaAdjunta> listarPorIdTicket(Long idTicket){
        return evidenciaAdjuntaRepository.findByIdTicket(idTicket);
    }

    public EvidenciaAdjunta obtenerPorIdEvidencia(Long idEvidencia){
        return evidenciaAdjuntaRepository.findByIdEvidencia(idEvidencia).orElseThrow(() -> new ResourceNotFoundException("Evidencia", id))''
    }

    //Adjuntar archivo a un ticket 
    public EvidenciaAdjunta adjuntarEvidencia(Long idTicket, EvidenciaAdjuntaDTO dto){
        log.info("Adjuntado evidencia al ticket {}", idTicket);

        TicketSoporte ticket = ticketSoporteRepository.findByIdTicket(idTicket).orElseThrow(() -> new ResourceNotFoundException("Ticket no encontrado", idTicket));

            EvidenciaAdjunta evidencia = EvidenciaAdjunta.builder()
                .nombreArchivo(dto.getNombreArchivo())
                .tipoArchivo(dto.getTipoArchivo())
                .urlArchivo(dto.getUrlArchivo())
                .fechaCarga(LocalDateTime.now())
                .ticket(ticket)
                .build();

        return evidenciaRepository.save(evidencia);
    }

    //Eliminar evidencia adjunta
    public void eliminarEvidencia(Integer id) {
        log.info("Eliminando evidencia {}", id);
        if (!evidenciaRepository.existsById(id))
            throw new ResourceNotFoundException("Evidencia", id);
        evidenciaRepository.deleteById(id);
    }
}
