package com.SoporteMicroServicio.SoporteM.service;
import java.time.LocalDateTime;
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

import com.SoporteMicroServicio.SoporteM.dto.CambiarEstadoDTO;
import com.SoporteMicroServicio.SoporteM.dto.CrearTicketDTO;
import com.SoporteMicroServicio.SoporteM.exception.BusinessException;
import com.SoporteMicroServicio.SoporteM.exception.ResourceNotFoundException;
import com.SoporteMicroServicio.SoporteM.feign.UsuarioFeignClient;
import com.SoporteMicroServicio.SoporteM.model.HistorialEstadoTicket;
import com.SoporteMicroServicio.SoporteM.model.TicketSoporte;
import com.SoporteMicroServicio.SoporteM.repository.HistorialEstadoTicketRepository;
import com.SoporteMicroServicio.SoporteM.repository.PersonalSoporteRepository;
import com.SoporteMicroServicio.SoporteM.repository.TicketSoporteRepository;


@ExtendWith(MockitoExtension.class)
class TicketSoporteServiceTest {
    
    @Mock
    private TicketSoporteRepository ticketSoporteRepository;

    @Mock
    private HistorialEstadoTicketRepository historialEstadoTicketRepository;

    @Mock
    private PersonalSoporteRepository personalSoporteRepository;

    @Mock
    private UsuarioFeignClient usuarioFeignClient;

    @InjectMocks
    private TicketSoporteService ticketSoporteService;

    @Test
    void listarTodosLosTickets(){
        TicketSoporte ticket1 = TicketSoporte.builder()
                .idTicket(1L)
                .runCliente(12345678L)
                .estadoTicket("ABIERTO")
                .build();
        TicketSoporte ticket2 = TicketSoporte.builder()
                .idTicket(2L)
                .runCliente(87654321L)
                .estadoTicket("EN_PROCESO")
                .build();
        when(ticketSoporteRepository.findAll()).thenReturn(List.of(ticket1, ticket2));

        List<TicketSoporte> resultado = ticketSoporteService.listarTodosLosTickets();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(ticketSoporteRepository, times(1)).findAll();
    }

    @Test
    void obtenerTicketPorId(){
        TicketSoporte ticket = TicketSoporte.builder()
                .idTicket(1L)
                .runCliente(12345678L)
                .estadoTicket("ABIERTO")
                .build();
        when(ticketSoporteRepository.findById(1L)).thenReturn(Optional.of(ticket));

        TicketSoporte resultado = ticketSoporteService.obtenerTicketPorId(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getIdTicket());
        assertEquals("ABIERTO", resultado.getEstadoTicket());
    }

    @Test
    void obtenerTicketPorId_null(){ //Id inesxistente 
        when(ticketSoporteRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> ticketSoporteService.obtenerTicketPorId(99L)); 
    }

    @Test
    void crearTicket(){
        CrearTicketDTO dto = new CrearTicketDTO();
        dto.setRunCliente(12345678L);
        dto.setAsunto("Producto dañado");
        dto.setDescripcion("El producto llegó completamente roto");
        dto.setTipoSolicitud("RECLAMO");
        dto.setCanal("WEB");
        dto.setPrioridad("ALTA");

        Map<String, Object> usuarioValido = new HashMap<>();
        usuarioValido.put("nombre", "Juan Perez");
        usuarioValido.put("estadoUsuario", "Activo");

        TicketSoporte ticketGuardado = TicketSoporte.builder()
                .idTicket(1L)
                .runCliente(12345678L)
                .estadoTicket("ABIERTO")
                .asunto("Producto dañado")
                .fechaCreacion(LocalDateTime.now())
                .build();
        
        when(usuarioFeignClient.obtenerUsuarioPorRut(12345678L)).thenReturn(usuarioValido);
        when(ticketSoporteRepository.save(any(TicketSoporte.class))).thenReturn(ticketGuardado);
        when(historialEstadoTicketRepository.save(any(HistorialEstadoTicket.class)))
            .thenReturn(new HistorialEstadoTicket());

        TicketSoporte resultado = ticketSoporteService.crearTicket(dto);

        assertNotNull(resultado);
        assertEquals("ABIERTO", resultado.getEstadoTicket());
        assertEquals(1L, resultado.getIdTicket());
        verify(ticketSoporteRepository, times(1)).save(any(TicketSoporte.class));
        verify(historialEstadoTicketRepository, times(1)).save(any(HistorialEstadoTicket.class));
    }

    @Test
    void crearTicket_null(){ //Con un usuario no valido o disponible
        CrearTicketDTO dto = new CrearTicketDTO();
        dto.setRunCliente(16543210L);
        dto.setAsunto("Error en el cobro");
        dto.setDescripcion("Se realizó un cobro duplicado en la compra realizada por el cliente");
        dto.setTipoSolicitud("RECLAMO");
        dto.setCanal("TELEFONO");
        dto.setPrioridad("ALTA");

        Map<String, Object> usuarioNoDisponible = new HashMap<>();
        usuarioNoDisponible.put("nombre", "Usuario no disponible");

        when(usuarioFeignClient.obtenerUsuarioPorRut(any(Long.class))).thenReturn(usuarioNoDisponible);

        assertThrows(BusinessException.class, () -> ticketSoporteService.crearTicket(dto));
    }

    @Test
    void crearTicket_clienteInactivo(){
        CrearTicketDTO dto = new CrearTicketDTO();
        dto.setRunCliente(18765432L);
        dto.setAsunto("Retraso en la entrega");
        dto.setDescripcion("El pedido aún no ha llegado y ya superó la fecha estimada de entrega");
        dto.setTipoSolicitud("CONSULTA");
        dto.setCanal("APP");
        dto.setPrioridad("MEDIA");

        Map<String, Object> usuarioInactivo = new HashMap<>();
        usuarioInactivo.put("nombre", "Pepe Tapia");
        usuarioInactivo.put("estadoUsuario", "Inactivo");

        when(usuarioFeignClient.obtenerUsuarioPorRut(18765432L)).thenReturn(usuarioInactivo);

        assertThrows(BusinessException.class, () -> ticketSoporteService.crearTicket(dto));
    }

    @Test
    void cambiarEstadoTicket(){ //Estado exitoso
        TicketSoporte ticket = TicketSoporte.builder()
                .idTicket(1L)
                .estadoTicket("ABIERTO")
                .build();

        CambiarEstadoDTO dto = new CambiarEstadoDTO();
        dto.setNuevoEstado("EN_PROCESO");
        dto.setUsuarioResponsable("admin");

        TicketSoporte ticketActualizado = TicketSoporte.builder()
                .idTicket(1L)
                .estadoTicket("EN_PROCESO")
                .build();

        when(ticketSoporteRepository.findById(1L)).thenReturn(Optional.of(ticket));
        when(ticketSoporteRepository.save(any(TicketSoporte.class))).thenReturn(ticketActualizado);

        TicketSoporte resultado = ticketSoporteService.cambiarEstado(1L, dto);

        assertNotNull(resultado);
        assertEquals("EN_PROCESO", resultado.getEstadoTicket());
        verify(ticketSoporteRepository, times(1)).save(any(TicketSoporte.class));
        }

    @Test
    void cambiarEstadoTicket_cerrado(){
        TicketSoporte ticketCerrado = TicketSoporte.builder()
                .idTicket(1L)
                .estadoTicket("CERRADO")
                .build();

        CambiarEstadoDTO dto = new CambiarEstadoDTO();
        dto.setNuevoEstado("EN_PROCESO");
        dto.setUsuarioResponsable("admin");

        when(ticketSoporteRepository.findById(1L)).thenReturn(Optional.of(ticketCerrado));

        assertThrows(BusinessException.class, () -> ticketSoporteService.cambiarEstado(1L, dto));
        }
    @Test
    void cerrarTicket_exitoso(){
        TicketSoporte ticket = TicketSoporte.builder()
                .idTicket(1L)
                .estadoTicket("EN_PROCESO")
                .build();

        TicketSoporte ticketCerrado = TicketSoporte.builder()
                .idTicket(1L)
                .estadoTicket("CERRADO")
                .fechaCierre(LocalDateTime.now())
                .build();
        
        when(ticketSoporteRepository.findById(1L)).thenReturn(Optional.of(ticket));
        when(ticketSoporteRepository.save(any(TicketSoporte.class))).thenReturn(ticketCerrado);

        TicketSoporte resultado = ticketSoporteService.cerrarTicket(1L, "admin");

        assertNotNull(resultado);
        assertEquals("CERRADO", resultado.getEstadoTicket());
        assertNotNull(resultado.getFechaCierre());
    }

    @Test
    void cambiarEstadoTicket_mismoEstado(){ 
        TicketSoporte ticket = TicketSoporte.builder()
                .idTicket(1L)
                .estadoTicket("ABIERTO")
                .build();
        
        CambiarEstadoDTO dto = new CambiarEstadoDTO();
        dto.setNuevoEstado("ABIERTO");
        dto.setUsuarioResponsable("admin");

        when(ticketSoporteRepository.findById(1L)).thenReturn(Optional.of(ticket));

        assertThrows(BusinessException.class, () -> ticketSoporteService.cambiarEstado(1L, dto));
    }

    @Test
    void cerrarTicket(){
        TicketSoporte ticketCerrado = TicketSoporte.builder()
                .idTicket(1L)
                .estadoTicket("CERRADO")
                .build();

        when(ticketSoporteRepository.findById(1L)).thenReturn(Optional.of(ticketCerrado));

        assertThrows(BusinessException.class, () -> ticketSoporteService.cerrarTicket(1L, "admin"));
    }

    @Test
    void eliminarTicket(){
        when(ticketSoporteRepository.existsById(1L)).thenReturn(true);

        ticketSoporteService.eliminarTicketSoporte(1L);

        verify(ticketSoporteRepository, times(1)).deleteById(1L);
    }

    @Test
    void eliminarTicketSoporte_null(){ //Con id inesxistente
        when(ticketSoporteRepository.existsById(99L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> ticketSoporteService.eliminarTicketSoporte(99L));
    }
}
