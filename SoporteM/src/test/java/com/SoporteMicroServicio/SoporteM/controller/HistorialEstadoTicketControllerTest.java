package com.SoporteMicroServicio.SoporteM.controller;
import java.util.List;

import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.SoporteMicroServicio.SoporteM.exception.ResourceNotFoundException;
import com.SoporteMicroServicio.SoporteM.model.HistorialEstadoTicket;
import com.SoporteMicroServicio.SoporteM.service.HistorialEstadoTicketService;

@WebMvcTest(HistorialEstadoTicketController.class)
class HistorialEstadoTicketControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private HistorialEstadoTicketService historialEstadoTicketService;

    @Test
    void listarPorIdTicket() throws Exception{
        HistorialEstadoTicket h1 = HistorialEstadoTicket.builder()
                .idHistorial(1L).estadoAnterior("ABIERTO").estadoNuevo("EN_PROCESO")
                .usuarioResponsable("admin").build();
        HistorialEstadoTicket h2 = HistorialEstadoTicket.builder()
                .idHistorial(2L).estadoAnterior("EN_PROCESO").estadoNuevo("CERRADO")
                .usuarioResponsable("admin").build();

        when(historialEstadoTicketService.listarPorIdTicket(1L)).thenReturn(List.of(h1, h2));

        mockMvc.perform(get("/api/v1/historialEstadoTicket/listar/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(2))
            .andExpect(jsonPath("$[0].estadoNuevo").value("EN_PROCESO"));
    }

    @Test
    void obtenerHistorialPorId_ok() throws Exception{
        HistorialEstadoTicket historial = HistorialEstadoTicket.builder()
                .idHistorial(1L).estadoAnterior("ABIERTO").estadoNuevo("EN_PROCESO")
                .usuarioResponsable("admin").build();

        when(historialEstadoTicketService.obtenerHistorialPorId(1L)).thenReturn(historial);

        mockMvc.perform(get("/api/v1/historialEstadoTicket/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.idHistorial").value(1))
            .andExpect(jsonPath("$.estadoNuevo").value("EN_PROCESO"));
    }

    @Test
    void obtenerHistorialPorId_noEncontrado() throws Exception{
        when(historialEstadoTicketService.obtenerHistorialPorId(99L))
                .thenThrow(new ResourceNotFoundException("HistorialEstadoTicket", 99L));

            mockMvc.perform(get("/api/v1/historialEstadoTicket/99"))
                .andExpect(status().isNotFound());
    }
}
