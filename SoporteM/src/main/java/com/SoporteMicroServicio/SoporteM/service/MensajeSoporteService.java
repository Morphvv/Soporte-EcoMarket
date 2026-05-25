package com.SoporteMicroServicio.SoporteM.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.SoporteMicroServicio.SoporteM.dto.MensajeSoporteDTO;
import com.SoporteMicroServicio.SoporteM.exception.BusinessException;
import com.SoporteMicroServicio.SoporteM.exception.ResourceNotFoundException;
import com.SoporteMicroServicio.SoporteM.model.MensajeSoporte;
import com.SoporteMicroServicio.SoporteM.model.TicketSoporte;
import com.SoporteMicroServicio.SoporteM.repository.MensajeSoporteRepository;
import com.SoporteMicroServicio.SoporteM.repository.TicketSoporteRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor

public class MensajeSoporteService {
    
    private final MensajeSoporteRepository mensajeSoporteRepository;
    private final TicketSoporteRepository ticketSoporteRepository;

    public List<MensajeSoporte> listarPorTicket(Long idTicket){
        return mensajeSoporteRepository.findByIdTicket(idTicket);
    }

    public MensajeSoporte obtenerPorId(Long idMensaje){
        return mensajeSoporteRepository.findByIdMensaje(idMensaje.orElseThrow() -> ResourceNotFoundException("Mensaje no encontrado", idMensaje));
    }

    //Agregar mensaje a un ticket ya existente
    public MensajeSoporte enviarMensaje(Long idTicket, MensajeSoporteDTO dto){
        log.info("Enviando mensaje al ticket id: {}", idTicket);

        TicketSoporte ticket = ticketSoporteRepository.findByIdTicket(idTicket).orElseThrow(() -> ResourceNotFoundException("Ticket no encontrado", idTicket));

        if ("Cerrado".equals(ticket.getEstadoTicket())){
            throw new BusinessException("No se pueden enviar mensajes a un ticket cerrado");
        }

        MensajeSoporte mensaje = MensajeSoporte.builder()
               .contenido(dto.getContenido())
                .remitente(dto.getRemitente())
                .tipoRemitente(dto.getTipoRemitente())
                .fechaEnvio(LocalDateTime.now())
                .ticket(ticket)
                .build();

        return mensajeRepository.save(mensaje);
    }

    //Responder mensaje 
    public MensajeSoporte responderMensaje(Long idMensajeOriginal, MensajeSoporteDTO dto){
        log.info("Respondiendo al mensaje {}", idMensajeOriginal);

        MensajeSoporte original = obtenerPorId(idMensajeOriginal);
        return enviarMensaje(original.getTicket().getIdTicket(), dto);

        public void eliminarMensaje(Integer id) {
        if (!mensajeRepository.existsById(id))
            throw new ResourceNotFoundException("Mensaje", id);
        mensajeRepository.deleteById(id);
    }
    
    
}
