package com.SoporteMicroServicio.SoporteM.service;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import com.SoporteMicroServicio.SoporteM.dto.MensajeSoporteDTO;
import com.SoporteMicroServicio.SoporteM.exception.BusinessException;
import com.SoporteMicroServicio.SoporteM.exception.ResourceNotFoundException;
import com.SoporteMicroServicio.SoporteM.model.MensajeSoporte;
import com.SoporteMicroServicio.SoporteM.model.TicketSoporte;
import com.SoporteMicroServicio.SoporteM.repository.MensajeSoporteRepository;
import com.SoporteMicroServicio.SoporteM.repository.TicketSoporteRepository;

import jakarta.annotation.Resource;

@ExtendWith(MockitoExtension.class)
class MensajeSoporteServiceTest {

    @Mock
    private MensajeSoporteRepository mensajeSoporteRepository;

    @Mock
    private TicketSoporteRepository ticketSoporteRepository;

    @InjectMocks
    private MensajeSoporteService mensajeSoporteService;

    @Test
    void listarPorIdTicket(){
        MensajeSoporte m1 = MensajeSoporte.builder().idMensaje(1L).contenido("Hola").build();
        MensajeSoporte m2 = MensajeSoporte.builder().idMensaje(2L).contenido("Gracias").build();
        when(mensajeSoporteRepository.findByTicketSoporteIdTicket(1L)).thenReturn(List.of(m1, m2));

        List<MensajeSoporte> resultado = mensajeSoporteService.listarPorIdTicket(1L);

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(mensajeSoporteRepository, times(1)).findByTicketSoporteIdTicket(1L);
    }

    @Test
    void obtenerMensajePorId(){
        MensajeSoporte mensaje = MensajeSoporte.builder().idMensaje(1L).contenido("Consulta").build();
        when(mensajeSoporteRepository.findById(1L)).thenReturn(Optional.of(mensaje));

        MensajeSoporte resultado = mensajeSoporteService.obtenerMensajePorId(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getIdMensaje());
    }

    @Test
    void obtenerMensajePorId_noExiste(){
        when(mensajeSoporteRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> mensajeSoporteService.obtenerMensajePorId(99L));
    }

    @Test
    void enviarMensaje(){
        TicketSoporte ticket = TicketSoporte.builder().idTicket(1L).estadoTicket("ABIERTO").build();
        MensajeSoporteDTO dto = new MensajeSoporteDTO();
        dto.setContenido("Necesito ayuda");
        dto.setRemitente("juan@eco.com");
        dto.setTipoRemitente("CLIENTE");

        MensajeSoporte guardado = MensajeSoporte.builder()
                .idMensaje(1L).contenido("Necesito ayuda").ticketSoporte(ticket).build();

        when(ticketSoporteRepository.findById(1L)).thenReturn(Optional.of(ticket));
        when(mensajeSoporteRepository.save(any(MensajeSoporte.class))).thenReturn(guardado);

        MensajeSoporte resultado = mensajeSoporteService.enviarMensaje(1L, dto);

        assertNotNull(resultado);
        assertEquals("Necesito ayuda", resultado.getContenido());
        verify(mensajeSoporteRepository, times(1)).save(any(MensajeSoporte.class));
    }

    @Test
    void enviarMensaje_ticketNoExiste(){
        when(ticketSoporteRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> mensajeSoporteService.enviarMensaje(99L, new MensajeSoporteDTO()));
    }

    @Test
    void enviarMensaje_ticketCerrado(){
        TicketSoporte ticketCerrado = TicketSoporte.builder().idTicket(1L).estadoTicket("CERRADO").build();
        when(ticketSoporteRepository.findById(1L)).thenReturn(Optional.of(ticketCerrado));

        assertThrows(BusinessException.class, () -> mensajeSoporteService.enviarMensaje(1L, new MensajeSoporteDTO()));
    }

    @Test
    void responderMensaje(){
        TicketSoporte ticket = TicketSoporte.builder().idTicket(1L).estadoTicket("ABIERTO").build();
        MensajeSoporte original = MensajeSoporte.builder().idMensaje(1L).ticketSoporte(ticket).build();
        MensajeSoporteDTO dto = new MensajeSoporteDTO();
        dto.setContenido("Respuesta del agente");
        dto.setRemitente("agente@eco.com");
        dto.setTipoRemitente("PERSONAL_SOPORTE");

        MensajeSoporte respuesta = MensajeSoporte.builder().idMensaje(2L).contenido("Respuesta del agente").build();

        when(mensajeSoporteRepository.findById(1L)).thenReturn(Optional.of(original));
        when(ticketSoporteRepository.findById(1L)).thenReturn(Optional.of(ticket));
        when(mensajeSoporteRepository.save(any(MensajeSoporte.class))).thenReturn(respuesta);

        MensajeSoporte resultado = mensajeSoporteService.responderMensaje(1L, dto);
        
        assertNotNull(resultado);
        verify(mensajeSoporteRepository, times(1)).save(any(MensajeSoporte.class));
    }

    @Test
    void eliminarMensaje(){
        when(mensajeSoporteRepository.existsById(1L)).thenReturn(true);

        mensajeSoporteService.eliminarMensaje(1L);

        verify(mensajeSoporteRepository, times(1)).deleteById(1l);
    }

    @Test
    void eliminarMensaje_noExiste(){
        when(mensajeSoporteRepository.existsById(99L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> mensajeSoporteService.eliminarMensaje(99L));
    }
    
}
