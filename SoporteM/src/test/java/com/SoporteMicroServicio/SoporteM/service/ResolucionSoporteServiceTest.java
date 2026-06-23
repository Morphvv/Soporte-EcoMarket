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

import com.SoporteMicroServicio.SoporteM.dto.ResolucionSoporteDTO;
import com.SoporteMicroServicio.SoporteM.exception.BusinessException;
import com.SoporteMicroServicio.SoporteM.exception.ResourceNotFoundException;
import com.SoporteMicroServicio.SoporteM.model.ResolucionSoporte;
import com.SoporteMicroServicio.SoporteM.model.TicketSoporte;
import com.SoporteMicroServicio.SoporteM.repository.ResolucionSoporteRepository;
import com.SoporteMicroServicio.SoporteM.repository.TicketSoporteRepository;

@ExtendWith(MockitoExtension.class)
class ResolucionSoporteServiceTest {

    @Mock
    private ResolucionSoporteRepository resolucionSoporteRepository;

    @Mock
    private TicketSoporteRepository ticketSoporteRepository;

    @InjectMocks
    private ResolucionSoporteService resolucionSoporteService;

    @Test
    void listarTodos(){
        ResolucionSoporte r1 = ResolucionSoporte.builder().idResolucion(1L).tipoResolucion("REEMBOLSO").build();
        ResolucionSoporte r2 = ResolucionSoporte.builder().idResolucion(2L).tipoResolucion("RECHAZO").build();
        when(resolucionSoporteRepository.findAll()).thenReturn(List.of(r1, r2));

        List<ResolucionSoporte> resultado = resolucionSoporteService.listarTodos();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(resolucionSoporteRepository, times(1)).findAll();
    }

    @Test
    void obtenerPorIdResolucion(){
        ResolucionSoporte resolucion = ResolucionSoporte.builder()
                .idResolucion(1L).tipoResolucion("REEMPLAZO").build();
        when(resolucionSoporteRepository.findById(1L)).thenReturn(Optional.of(resolucion));

        ResolucionSoporte resultado = resolucionSoporteService.obtenerPorIdResolucion(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getIdResolucion());
    }

    @Test
    void obtenerPorIdResolucion_noExiste(){
        when(resolucionSoporteRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> resolucionSoporteService.obtenerPorIdResolucion(99L));
    }

    @Test
    void regigstrarResolucion(){
        TicketSoporte ticket = TicketSoporte.builder().idTicket(1L).estadoTicket("EN_PROCESO").build();
        ResolucionSoporteDTO dto = new ResolucionSoporteDTO();
        dto.setTipoResolucion("REEMBOLSO");
        dto.setDescripcion("Se aprueba el reembolso por producto defectuoso");
        dto.setAprobadoPor("Supervisor Juan");

        ResolucionSoporte guardada = ResolucionSoporte.builder()
                .idResolucion(1L).tipoResolucion("REEMBOLSO").ticketSoporte(ticket).build();

        when(ticketSoporteRepository.findById(1L)).thenReturn(Optional.of(ticket));
        when(resolucionSoporteRepository.findByTicketSoporteIdTicket(1L)).thenReturn(Optional.empty());
        when(resolucionSoporteRepository.save(any(ResolucionSoporte.class))).thenReturn(guardada);

        ResolucionSoporte resultado = resolucionSoporteService.registrarResolucion(1L, dto);

        assertNotNull(resultado);
        assertEquals("REEMBOLSO", resultado.getTipoResolucion());
        verify(resolucionSoporteRepository, times(1)).save(any(ResolucionSoporte.class));
    }

    @Test
    void registrarResolucion_ticketNoExiste(){
        when(ticketSoporteRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> resolucionSoporteService.registrarResolucion(99L, new ResolucionSoporteDTO()));
    }

    @Test
    void registrarResolucion_yaExiste(){
        TicketSoporte ticket = TicketSoporte.builder().idTicket(1L).build();
        ResolucionSoporte existente = ResolucionSoporte.builder().idResolucion(1L).build();

        when(ticketSoporteRepository.findById(1L)).thenReturn(Optional.of(ticket));
        when(resolucionSoporteRepository.findByTicketSoporteIdTicket(1L)).thenReturn(Optional.of(existente));

        assertThrows(BusinessException.class, () -> resolucionSoporteService.registrarResolucion(1L, new ResolucionSoporteDTO()));
    }

    @Test
    void modificarResolucion(){
        ResolucionSoporte resolucion = ResolucionSoporte.builder()
                .idResolucion(1L).tipoResolucion("REEMBOLSO").descripcion("Descripcion vieja").build();

        ResolucionSoporteDTO dto = new ResolucionSoporteDTO();
        dto.setTipoResolucion("DEVOLUCION");
        dto.setDescripcion("Nueva descripcion actualizada correctamente");
        dto.setAprobadoPor("Gerente Pedro");

        when(resolucionSoporteRepository.findById(1L)).thenReturn(Optional.of(resolucion));
        when(resolucionSoporteRepository.save(any(ResolucionSoporte.class))).thenReturn(resolucion);

        ResolucionSoporte resultado = resolucionSoporteService.modificarResolucion(1L, dto);

        assertNotNull(resultado);
        verify(resolucionSoporteRepository, times(1)).save(any(ResolucionSoporte.class));
    }
}
