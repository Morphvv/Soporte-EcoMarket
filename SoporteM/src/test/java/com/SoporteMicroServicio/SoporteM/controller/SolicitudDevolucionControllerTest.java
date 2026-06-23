package com.SoporteMicroServicio.SoporteM.controller;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.SoporteMicroServicio.SoporteM.dto.SolicitudDevolucionDTO;
import com.SoporteMicroServicio.SoporteM.exception.BusinessException;
import com.SoporteMicroServicio.SoporteM.exception.ResourceNotFoundException;
import com.SoporteMicroServicio.SoporteM.model.SolicitudDevolucion;
import com.SoporteMicroServicio.SoporteM.service.SolicitudDevolucionService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(SolicitudDevolucionController.class)
class SolicitudDevolucionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private SolicitudDevolucionService solicitudDevolucionService;

    @Test
    void listarTodas_ok() throws Exception{
        SolicitudDevolucion s1 = SolicitudDevolucion.builder()
                .idSolicitudD(1L).estadoSolicitud("PENDIENTE").build();
        SolicitudDevolucion s2 = SolicitudDevolucion.builder()
                .idSolicitudD(2L).estadoSolicitud("APROBADA").build();

        when(solicitudDevolucionService.listarTodos()).thenReturn(List.of(s1, s2));

        mockMvc.perform(get("/api/v1/solicitudDevolucion/listar"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void listarPorId_noEncontrado() throws Exception{
        when(solicitudDevolucionService.obtenerPorIdDevolucion(99L))
            .thenThrow(new ResourceNotFoundException("SolicitudDevolucion", 99L));

            mockMvc.perform(get("/api/v1/solicitudDevolucion/listar/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void registrarSolicitud_ok() throws Exception{
        SolicitudDevolucionDTO dto = new SolicitudDevolucionDTO();
        dto.setIdPedido(10L);
        dto.setIdProducto(5L);
        dto.setCantidad(2);
        dto.setMotivo("Producto defectuoso");

        SolicitudDevolucion creada = SolicitudDevolucion.builder()
                .idSolicitudD(1L).estadoSolicitud("PENDIENTE").build();

        when(solicitudDevolucionService.registrarSolicitud(eq(1L), any(SolicitudDevolucionDTO.class)))
            .thenReturn(creada);

        mockMvc.perform(post("/api/v1/solicitudDevolucion/registrar/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.idSolicitudD").value(1))
            .andExpect(jsonPath("$.estadoSolicitud").value("PENDIENTE"));
    }

    @Test
    void validarProducto_ok() throws Exception{
        when(solicitudDevolucionService.validarProducto(1L)).thenReturn(true);

        mockMvc.perform(get("/api/v1/solicitudDevolucion/validar/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.valido").value(true));
    }

    @Test
    void aprobarDevolucion_ok() throws Exception {
        SolicitudDevolucion aprobada = SolicitudDevolucion.builder()
                .idSolicitudD(1L).estadoSolicitud("APROBADA").build();

        when(solicitudDevolucionService.aprobarSolicitud(1L)).thenReturn(aprobada);

        mockMvc.perform(put("/api/v1/solicitudDevolucion/aprobar/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estadoSolicitud").value("APROBADA"));
    }

    @Test
    void rechazarDevolucion() throws Exception{
        when(solicitudDevolucionService.rechazarDevolucion(1L))
            .thenThrow(new BusinessException("Solo se puede rechazar solicitudes en estado PENDIENTE"));

    mockMvc.perform(put("/api/v1/solicitudDevolucion/rechazar/1"))
                .andExpect(status().isBadRequest());
    }
}
