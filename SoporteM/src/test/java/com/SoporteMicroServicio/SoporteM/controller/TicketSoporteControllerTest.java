package com.SoporteMicroServicio.SoporteM.controller;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.SoporteMicroServicio.SoporteM.dto.CambiarEstadoDTO;
import com.SoporteMicroServicio.SoporteM.dto.CrearTicketDTO;
import com.SoporteMicroServicio.SoporteM.exception.ResourceNotFoundException;
import com.SoporteMicroServicio.SoporteM.model.TicketSoporte;
import com.SoporteMicroServicio.SoporteM.service.TicketSoporteService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(TicketSoporteController.class)
class TicketSoporteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private TicketSoporteService ticketSoporteService;


    @Test
    void listarTodosTickets_ok() throws Exception{
        TicketSoporte t1 = TicketSoporte.builder()
                .idTicket(1L).estadoTicket("ABIERTO").runCliente(12345678L).build();
        TicketSoporte t2 = TicketSoporte.builder()
                .idTicket(2L).estadoTicket("EN_PROCESO").runCliente(87654321L).build();

        when(ticketSoporteService.listarTodosLosTickets()).thenReturn(List.of(t1, t2));

        mockMvc.perform(get("/api/v1/ticketSoporte/listar"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(2))
            .andExpect(jsonPath("$[0].estadoTicket").value("ABIERTO"));
    }

    @Test
    void listarTicketPorId_ok() throws Exception{
        TicketSoporte ticket = TicketSoporte.builder()
                .idTicket(1L).estadoTicket("ABIERTO").runCliente(12345678L).build();

        when(ticketSoporteService.obtenerTicketPorId(1L)).thenReturn(ticket);

        mockMvc.perform(get("/api/v1/ticketSoporte/listar/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.idTicket").value(1))
            .andExpect(jsonPath("$.estadoTicket").value("ABIERTO"));

        }

    
    @Test
    void listarTicketPorId_noEncontrado() throws Exception{
        when(ticketSoporteService.obtenerTicketPorId(99L)).thenThrow(new ResourceNotFoundException("Ticket", 99L));

        mockMvc.perform(get("/api/v1/ticketSoporte/listar/99"))
            .andExpect(status().isNotFound());

    }
    
    @Test
    void crearTicket_ok() throws Exception{
    CrearTicketDTO dto = new CrearTicketDTO();
    dto.setRunCliente(12345678L);
    dto.setAsunto("Producto dañado");
    dto.setDescripcion("El producto llegó roto");
    dto.setTipoSolicitud("RECLAMO");
    dto.setCanal("WEB");
    dto.setPrioridad("ALTA");

    TicketSoporte creado = TicketSoporte.builder()
        .idTicket(1L)
        .estadoTicket("ABIERTO")
        .runCliente(12345678L)
        .build();

    when(ticketSoporteService.crearTicket(any(CrearTicketDTO.class))).thenReturn(creado);

    mockMvc.perform(post("/api/v1/ticketSoporte/crear")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(dto)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.idTicket").value(1))
        .andExpect(jsonPath("$.estadoTicket").value("ABIERTO"));
    }

    @Test
    void eliminarTicket_ok() throws Exception{
        mockMvc.perform(delete("/api/v1/ticketSoporte/eliminar/1"))
            .andExpect(status().isNoContent());
    }

    @Test
    void eliminarTicket_noEncontrado() throws Exception{
    doThrow(new ResourceNotFoundException("Ticket", 99L))
            .when(ticketSoporteService).eliminarTicketSoporte(99L);

    mockMvc.perform(delete("/api/v1/ticketSoporte/eliminar/99"))
        .andExpect(status().isNotFound());
    }

    @Test
    void listarPorCliente_ok() throws Exception {
        TicketSoporte t1 = TicketSoporte.builder()
                .idTicket(1L).runCliente(12345678L).estadoTicket("ABIERTO").build();

        when(ticketSoporteService.listarPorCliente(12345678L)).thenReturn(List.of(t1));

        mockMvc.perform(get("/api/v1/ticketSoporte/cliente/12345678"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(1))
            .andExpect(jsonPath("$[0].runCliente").value(12345678));
    }

    @Test
    void listarPorEstado_ok() throws Exception {
        TicketSoporte t1 = TicketSoporte.builder()
                .idTicket(1L).estadoTicket("ABIERTO").build();
        TicketSoporte t2 = TicketSoporte.builder()
                .idTicket(2L).estadoTicket("ABIERTO").build();

        when(ticketSoporteService.listarPorEstado("ABIERTO")).thenReturn(List.of(t1, t2));

        mockMvc.perform(get("/api/v1/ticketSoporte/estado/ABIERTO"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void cambiarEstado_ok() throws Exception {
        CambiarEstadoDTO dto = new CambiarEstadoDTO();
        dto.setNuevoEstado("EN_PROCESO");
        dto.setUsuarioResponsable("admin");

        TicketSoporte actualizado = TicketSoporte.builder()
                .idTicket(1L).estadoTicket("EN_PROCESO").build();

        when(ticketSoporteService.cambiarEstado(eq(1L), any(CambiarEstadoDTO.class)))
                .thenReturn(actualizado);

        mockMvc.perform(put("/api/v1/ticketSoporte/cambiarEstado/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.estadoTicket").value("EN_PROCESO"));
    }

    @Test
    void cerrarTicket_ok() throws Exception {
        TicketSoporte cerrado = TicketSoporte.builder()
                .idTicket(1L).estadoTicket("CERRADO").build();

        when(ticketSoporteService.cerrarTicket(eq(1L), eq("admin"))).thenReturn(cerrado);

        mockMvc.perform(put("/api/v1/ticketSoporte/cerrar/1")
                .param("usuarioResponsable", "admin"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.estadoTicket").value("CERRADO"));
    }

    @Test
    void clasificarSolicitud_ok() throws Exception {
        TicketSoporte clasificado = TicketSoporte.builder()
                .idTicket(1L).estadoTicket("ABIERTO").prioridad("ALTA").build();

        when(ticketSoporteService.clasificarSolicitud(eq(1L), eq("ALTA"), eq(null)))
                .thenReturn(clasificado);

        mockMvc.perform(put("/api/v1/ticketSoporte/clasificar/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"prioridad\":\"ALTA\"}"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.prioridad").value("ALTA"));
    }

    @Test
    void crearTicket_validacionFalla() throws Exception {
        mockMvc.perform(post("/api/v1/ticketSoporte/crear")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
            .andExpect(status().isBadRequest());
    }

    @Test
    void listarTodos_errorInterno() throws Exception {
        when(ticketSoporteService.listarTodosLosTickets())
            .thenThrow(new RuntimeException("Error inesperado"));

        mockMvc.perform(get("/api/v1/ticketSoporte/listar"))
            .andExpect(status().isInternalServerError());
    }

    @Test
    void clasificarSolicitud_conPersonal() throws Exception {
        TicketSoporte clasificado = TicketSoporte.builder()
                .idTicket(1L).estadoTicket("ABIERTO").prioridad("ALTA").build();

        when(ticketSoporteService.clasificarSolicitud(eq(1L), eq("ALTA"), eq(123L)))
                .thenReturn(clasificado);

        mockMvc.perform(put("/api/v1/ticketSoporte/clasificar/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"prioridad\":\"ALTA\",\"idPersonal\":123}"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.prioridad").value("ALTA"));
    }

}
