package com.SoporteMicroServicio.SoporteM.service;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.SoporteMicroServicio.SoporteM.dto.SolicitudDevolucionDTO;
import com.SoporteMicroServicio.SoporteM.exception.BusinessException;
import com.SoporteMicroServicio.SoporteM.exception.ResourceNotFoundException;
import com.SoporteMicroServicio.SoporteM.model.SolicitudDevolucion;
import com.SoporteMicroServicio.SoporteM.model.TicketSoporte;
import com.SoporteMicroServicio.SoporteM.repository.SolicitudDevolucionRepository;
import com.SoporteMicroServicio.SoporteM.repository.TicketSoporteRepository;

@ExtendWith(MockitoExtension.class)
class SolicitudDevolucionServiceTest {

    @Mock
    private SolicitudDevolucionRepository solicitudDevolucionRepository;

    @Mock
    private TicketSoporteRepository ticketSoporteRepository;

    @InjectMocks
    private SolicitudDevolucionService solicitudDevolucionService;

    @Test
    void listarTodos(){
        SolicitudDevolucion s1 = SolicitudDevolucion.builder().idSolicitudD(1L).estadoSolicitud("PENDIENTE").build();
        SolicitudDevolucion s2 = SolicitudDevolucion.builder().idSolicitudD(2L).estadoSolicitud("APROBADA").build();
        when(solicitudDevolucionRepository.findAll()).thenReturn(List.of(s1, s2));

        List<SolicitudDevolucion> resultado = solicitudDevolucionService.listarTodos();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
    }

    @Test
    void obtenerPorIdDevolucion(){
        SolicitudDevolucion solicitud = SolicitudDevolucion.builder()
                .idSolicitudD(1L).estadoSolicitud("PENDIENTE").build();
        when(solicitudDevolucionRepository.findById(1L)).thenReturn(Optional.of(solicitud));

        SolicitudDevolucion resultado = solicitudDevolucionService.obtenerPorIdDevolucion(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getIdSolicitudD());
    }

    @Test
    void obtenerPorIdDevolucion_noExiste(){
        when(solicitudDevolucionRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> solicitudDevolucionService.obtenerPorIdDevolucion(99L));    
    }

    @Test
    void registrarSolicitud(){
        TicketSoporte ticket = TicketSoporte.builder().idTicket(1L).estadoTicket("ABIERTO").build();
        SolicitudDevolucionDTO dto = new SolicitudDevolucionDTO();
        dto.setIdPedido(10L);
        dto.setIdProducto(20L);
        dto.setCantidad(2);
        dto.setMotivo("Producto defectuoso recibido");

        SolicitudDevolucion guardada = SolicitudDevolucion.builder()
                .idSolicitudD(1L).estadoSolicitud("PENDIENTE").ticketSoporte(ticket).build();

        when(ticketSoporteRepository.findById(1L)).thenReturn(Optional.of(ticket));
        when(solicitudDevolucionRepository.findByTicketSoporteIdTicket(1L)).thenReturn(Optional.empty());
        when(solicitudDevolucionRepository.save(any(SolicitudDevolucion.class))).thenReturn(guardada);

        SolicitudDevolucion resultado = solicitudDevolucionService.registrarSolicitud(1L, dto);

        assertNotNull(resultado);
        assertEquals("PENDIENTE", resultado.getEstadoSolicitud());
        verify(solicitudDevolucionRepository, times(1)).save(any(SolicitudDevolucion.class));
    }

    @Test
    void registrarSolicitud_noExiste(){
        when(ticketSoporteRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> solicitudDevolucionService.registrarSolicitud(99L, new SolicitudDevolucionDTO()));
    }

    @Test
    void registrarSolicitud_yaExiste(){
        TicketSoporte ticket = TicketSoporte.builder().idTicket(1L).build();
        SolicitudDevolucion existente = SolicitudDevolucion.builder().idSolicitudD(1L).build();

        when(ticketSoporteRepository.findById(1L)).thenReturn(Optional.of(ticket));
        when(solicitudDevolucionRepository.findByTicketSoporteIdTicket(1L)).thenReturn(Optional.of(existente));

        assertThrows(BusinessException.class,
                () -> solicitudDevolucionService.registrarSolicitud(1L, new SolicitudDevolucionDTO()));
    }

    @Test
    void aprobarSolicitud(){
        SolicitudDevolucion solicitud = SolicitudDevolucion.builder()
                .idSolicitudD(1L).idProducto(5L).cantidad(2).estadoSolicitud("PENDIENTE").build();

        when(solicitudDevolucionRepository.findById(1L)).thenReturn(Optional.of(solicitud));
        when(solicitudDevolucionRepository.save(any(SolicitudDevolucion.class))).thenReturn(solicitud);

        SolicitudDevolucion resultado = solicitudDevolucionService.aprobarSolicitud(1L);

        assertEquals("APROBADA", resultado.getEstadoSolicitud());
        verify(solicitudDevolucionRepository, times(1)).save(any(SolicitudDevolucion.class));
    }

    @Test
    void rechazarSolicitud(){
        SolicitudDevolucion solicitud = SolicitudDevolucion.builder()
                .idSolicitudD(1L).estadoSolicitud("PENDIENTE").build();
        SolicitudDevolucion rechazada = SolicitudDevolucion.builder()
                .idSolicitudD(1L).estadoSolicitud("RECHAZADA").build();

        when(solicitudDevolucionRepository.findById(1L)).thenReturn(Optional.of(solicitud));
        when(solicitudDevolucionRepository.save(any(SolicitudDevolucion.class))).thenReturn(rechazada);

        SolicitudDevolucion resultado = solicitudDevolucionService.rechazarDevolucion(1L);

        assertEquals("RECHAZADA", resultado.getEstadoSolicitud());
        verify(solicitudDevolucionRepository, times(1)).save(any(SolicitudDevolucion.class));
    }

    @Test
    void aprobarSolicitud_productoInvalido() {
        SolicitudDevolucion solicitud = SolicitudDevolucion.builder()
                .idSolicitudD(1L).idProducto(null).cantidad(null).estadoSolicitud("PENDIENTE").build();

        when(solicitudDevolucionRepository.findById(1L)).thenReturn(Optional.of(solicitud));

        assertThrows(BusinessException.class, () -> solicitudDevolucionService.aprobarSolicitud(1L));
    }

    @Test
    void rechazarSolicitud_noEsPendiente() {
        SolicitudDevolucion solicitud = SolicitudDevolucion.builder()
                .idSolicitudD(1L).estadoSolicitud("APROBADA").build();

        when(solicitudDevolucionRepository.findById(1L)).thenReturn(Optional.of(solicitud));

        assertThrows(BusinessException.class, () -> solicitudDevolucionService.rechazarDevolucion(1L));
    }

    @Test
    void validarProducto_productoNulo() {
        SolicitudDevolucion solicitud = SolicitudDevolucion.builder()
                .idSolicitudD(1L).idProducto(null).cantidad(2).estadoSolicitud("PENDIENTE").build();

        when(solicitudDevolucionRepository.findById(1L)).thenReturn(Optional.of(solicitud));

        Boolean resultado = solicitudDevolucionService.validarProducto(1L);
        assertEquals(false, resultado);
    }

    @Test
    void validarProducto_cantidadNula() {
        SolicitudDevolucion solicitud = SolicitudDevolucion.builder()
                .idSolicitudD(1L).idProducto(5L).cantidad(null).estadoSolicitud("PENDIENTE").build();

        when(solicitudDevolucionRepository.findById(1L)).thenReturn(Optional.of(solicitud));

        Boolean resultado = solicitudDevolucionService.validarProducto(1L);
        assertEquals(false, resultado);
    }

    @Test
    void validarProducto_cantidadCero() {
        SolicitudDevolucion solicitud = SolicitudDevolucion.builder()
                .idSolicitudD(1L).idProducto(5L).cantidad(0).estadoSolicitud("PENDIENTE").build();

        when(solicitudDevolucionRepository.findById(1L)).thenReturn(Optional.of(solicitud));

        Boolean resultado = solicitudDevolucionService.validarProducto(1L);
        assertEquals(false, resultado);
    }
}
