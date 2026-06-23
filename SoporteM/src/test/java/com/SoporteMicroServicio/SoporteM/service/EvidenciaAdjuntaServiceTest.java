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

import com.SoporteMicroServicio.SoporteM.dto.EvidenciaAdjuntaDTO;
import com.SoporteMicroServicio.SoporteM.exception.ResourceNotFoundException;
import com.SoporteMicroServicio.SoporteM.model.EvidenciaAdjunta;
import com.SoporteMicroServicio.SoporteM.model.TicketSoporte;
import com.SoporteMicroServicio.SoporteM.repository.EvidenciaAdjuntaRepository;
import com.SoporteMicroServicio.SoporteM.repository.TicketSoporteRepository;


@ExtendWith(MockitoExtension.class)
class EvidenciaAdjuntaServiceTest {

    @Mock
    private EvidenciaAdjuntaRepository evidenciaAdjuntaRepository;

    @Mock
    private TicketSoporteRepository ticketSoporteRepository;

    @InjectMocks
    private EvidenciaAdjuntaService evidenciaAdjuntaService;

    @Test
    void listarPorIdTicket(){
        EvidenciaAdjunta e1 = EvidenciaAdjunta.builder().idEvidencia(1L).nombreArchivo("foto.jpg").build();
        EvidenciaAdjunta e2 = EvidenciaAdjunta.builder().idEvidencia(2L).nombreArchivo("doc.pdf").build();
        when(evidenciaAdjuntaRepository.findByTicketSoporteIdTicket(1L)).thenReturn(List.of(e1, e2));

        List<EvidenciaAdjunta> resultado = evidenciaAdjuntaService.listarPorIdTicket(1L);

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
    }

    @Test
    void obtenerEvidenciaPorId(){
        EvidenciaAdjunta evidencia = EvidenciaAdjunta.builder()
                .idEvidencia(1L).nombreArchivo("foto.jpg").tipoArchivo("IMAGEN").build();
        when(evidenciaAdjuntaRepository.findById(1L)).thenReturn(Optional.of(evidencia));

        EvidenciaAdjunta resultado = evidenciaAdjuntaService.obtenerEvidenciaPorId(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getIdEvidencia());
    }

    @Test
    void obtenerEvidenciaPorId_noExiste(){
        when(evidenciaAdjuntaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> evidenciaAdjuntaService.obtenerEvidenciaPorId(99L));
    }

    @Test
    void adjuntarEvidencia(){
        TicketSoporte ticket = TicketSoporte.builder().idTicket(1L).estadoTicket("ABIERTO").build();
        EvidenciaAdjuntaDTO dto = new EvidenciaAdjuntaDTO();
        dto.setNombreArchivo("comprobante.pdf");
        dto.setTipoArchivo("PDF");
        dto.setUrlArchivo("http://storage/comprobante.pdf");

        EvidenciaAdjunta guardada = EvidenciaAdjunta.builder()
                .idEvidencia(1L).nombreArchivo("comprobante.pdf").ticketSoporte(ticket).build();
        
        when(ticketSoporteRepository.findById(1L)).thenReturn(Optional.of(ticket));
        when(evidenciaAdjuntaRepository.save(any(EvidenciaAdjunta.class))).thenReturn(guardada);

        EvidenciaAdjunta resultado = evidenciaAdjuntaService.adjuntarEvidencia(1L, dto);

        assertNotNull(resultado);
        assertEquals("comprobante.pdf", resultado.getNombreArchivo());
        verify(evidenciaAdjuntaRepository, times(1)).save(any(EvidenciaAdjunta.class));
    }
    
    @Test
    void adjuntarEvidencia_ticketNoExiste(){
        when(ticketSoporteRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> evidenciaAdjuntaService.adjuntarEvidencia(99L, new EvidenciaAdjuntaDTO()));
    }

    @Test
    void eliminarEvidencia(){
        when(evidenciaAdjuntaRepository.existsById(1L)).thenReturn(true);

        evidenciaAdjuntaService.eliminarEvidencia(1L);
        verify(evidenciaAdjuntaRepository, times(1)).deleteById(1L);
    }

    @Test
    void eliminarEvidencia_noExiste(){
        when(evidenciaAdjuntaRepository.existsById(99L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> evidenciaAdjuntaService.eliminarEvidencia(99L));
    }
}
