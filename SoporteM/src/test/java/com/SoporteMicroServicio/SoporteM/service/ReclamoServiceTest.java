package com.SoporteMicroServicio.SoporteM.service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

import com.SoporteMicroServicio.SoporteM.dto.ReclamoDTO;
import com.SoporteMicroServicio.SoporteM.exception.BusinessException;
import com.SoporteMicroServicio.SoporteM.exception.ResourceNotFoundException;
import com.SoporteMicroServicio.SoporteM.feign.PedidoFeignClient;
import com.SoporteMicroServicio.SoporteM.model.Reclamo;
import com.SoporteMicroServicio.SoporteM.model.TicketSoporte;
import com.SoporteMicroServicio.SoporteM.repository.ReclamoRepository;
import com.SoporteMicroServicio.SoporteM.repository.TicketSoporteRepository;

@ExtendWith(MockitoExtension.class)
class ReclamoServiceTest {

    @Mock
    private ReclamoRepository reclamoRepository;
    
    @Mock
    private TicketSoporteRepository ticketSoporteRepository;

    @Mock
    private PedidoFeignClient pedidoFeignClient;

    @InjectMocks
    private ReclamoService reclamoService;

    @Test
    void listarTodosReclamos(){
        Reclamo r1 = Reclamo.builder().idReclamo(1L).estadoReclamo("EN_REVISION").build();
        Reclamo r2 = Reclamo.builder().idReclamo(2L).estadoReclamo("RESUELTO").build();
        when(reclamoRepository.findAll()).thenReturn(List.of(r1, r2));

        List<Reclamo> resultado = reclamoService.listarTodosReclamos();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(reclamoRepository, times(1)).findAll();
    }

    @Test
    void obtenerPorIdReclamo(){
        Reclamo reclamo = Reclamo.builder().idReclamo(1L).estadoReclamo("EN_REVISION").build();
        when(reclamoRepository.findById(1L)).thenReturn(Optional.of(reclamo));

        Reclamo resultado = reclamoService.obtenerPorIdReclamo(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getIdReclamo());
    }

    @Test
    void obtenerPorIdReclamo_noExiste(){
        when(reclamoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> reclamoService.obtenerPorIdReclamo(99L));
    }

    @Test
    void registrarReclamo(){
        TicketSoporte ticket = TicketSoporte.builder().idTicket(1L).estadoTicket("ABIERTO").build();
        ReclamoDTO dto = new ReclamoDTO();
        dto.setIdPedido(10L);
        dto.setIdProducto(20L);
        dto.setMotivo("Producto defectuoso");
        dto.setDescripcion("El producto llegó roto");

        Reclamo reclamoGuardado = Reclamo.builder()
                .idReclamo(1L)
                .estadoReclamo("EN_REVISION")
                .ticketSoporte(ticket)
                .build();

        Map<String, Object> pedidoValido = new HashMap<>();
        pedidoValido.put("estado", "PENDIENTE");

        when(ticketSoporteRepository.findById(1L)).thenReturn(Optional.of(ticket));
        when(reclamoRepository.findByTicketSoporteIdTicket(1l)).thenReturn(Optional.empty());
        when(pedidoFeignClient.obtenerPedidoPorId(10L)).thenReturn(pedidoValido);
        when(reclamoRepository.save(any(Reclamo.class))).thenReturn(reclamoGuardado);

        Reclamo resultado = reclamoService.registrarReclamo(1L, dto);

        assertNotNull(resultado);
        assertEquals("EN_REVISION", resultado.getEstadoReclamo());
        verify(reclamoRepository, times(1)).save(any(Reclamo.class));
    }

    @Test
    void registrarReclamo_pedidoNoValidado(){
        TicketSoporte ticket = TicketSoporte.builder().idTicket(1L).estadoTicket("ABIERTO").build();
        ReclamoDTO dto = new ReclamoDTO();
        dto.setIdPedido(99L);
        dto.setIdProducto(20L);
        dto.setMotivo("Producto defectuoso");
        dto.setDescripcion("El producto llegó roto");

        Map<String, Object> pedidoNoDisponible = new HashMap<>();
        pedidoNoDisponible.put("estado", "DESCONOCIDO");

        when(ticketSoporteRepository.findById(1L)).thenReturn(Optional.of(ticket));
        when(reclamoRepository.findByTicketSoporteIdTicket(1L)).thenReturn(Optional.empty());
        when(pedidoFeignClient.obtenerPedidoPorId(99L)).thenReturn(pedidoNoDisponible);

        assertThrows(BusinessException.class, () -> reclamoService.registrarReclamo(1L, dto));
    }

    @Test
    void registrarReclamo_ticketNoExiste(){
        when(ticketSoporteRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> reclamoService.registrarReclamo(99L, new ReclamoDTO()));
    }

    @Test
    void resigtrarReclamo_yaExiste(){
        TicketSoporte ticket = TicketSoporte.builder().idTicket(1L).build();
        Reclamo existente = Reclamo.builder().idReclamo(1L).build();

        when(ticketSoporteRepository.findById(1L)).thenReturn(Optional.of(ticket));
        when(reclamoRepository.findByTicketSoporteIdTicket(1L)).thenReturn(Optional.of(existente));

        assertThrows(BusinessException.class, () -> reclamoService.registrarReclamo(1L, new ReclamoDTO()));
    }

    @Test
    void revisarReclamo(){
        Reclamo reclamo = Reclamo.builder().idReclamo(1L).estadoReclamo("EN_REVISION").build();
        when(reclamoRepository.findById(1L)).thenReturn(Optional.of(reclamo));
        when(reclamoRepository.save(any(Reclamo.class))).thenReturn(reclamo);

        Reclamo resultado = reclamoService.revisarReclamo(1L);

        assertEquals("REVISADO", resultado.getEstadoReclamo());
    }

    @Test
    void actualizarEstado_valido(){
        Reclamo reclamo = Reclamo.builder().idReclamo(1L).estadoReclamo("EN_REVISION").build();
        when(reclamoRepository.findById(1L)).thenReturn(Optional.of(reclamo));
        when(reclamoRepository.save(any(Reclamo.class))).thenReturn(reclamo);

        Reclamo resultado = reclamoService.actualizarEstado(1L, "RESUELTO");

        assertEquals("RESUELTO", resultado.getEstadoReclamo());
    }

    @Test
    void actualizarEstado_invalido(){
        assertThrows(BusinessException.class, () -> reclamoService.actualizarEstado(1L, "ESTADO_INVENTADO"));
    }

    
}
