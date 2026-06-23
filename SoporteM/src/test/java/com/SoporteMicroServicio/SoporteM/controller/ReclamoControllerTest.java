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

import com.SoporteMicroServicio.SoporteM.dto.ReclamoDTO;
import com.SoporteMicroServicio.SoporteM.exception.BusinessException;
import com.SoporteMicroServicio.SoporteM.exception.ResourceNotFoundException;
import com.SoporteMicroServicio.SoporteM.model.Reclamo;
import com.SoporteMicroServicio.SoporteM.service.ReclamoService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(ReclamoController.class)
class ReclamoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ReclamoService reclamoService;

    @Test
    void listarTodos_ok() throws Exception{
        Reclamo r1 = Reclamo.builder().idReclamo(1L).estadoReclamo("EN_REVISION").build();
        Reclamo r2 = Reclamo.builder().idReclamo(2L).estadoReclamo("RESUELTO").build();

        when(reclamoService.listarTodosReclamos()).thenReturn(List.of(r1, r2));

        mockMvc.perform(get("/api/v1/reclamo/listar"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(2))
            .andExpect(jsonPath("$[0].estadoReclamo").value("EN_REVISION"));
    }

    @Test
    void obtenerReclamo_ok() throws Exception{
        Reclamo reclamo = Reclamo.builder()
                .idReclamo(1L)
                .estadoReclamo("EN_REVISION")
                .motivo("Producto defectuoso")
                .build();
        
        when(reclamoService.obtenerPorIdReclamo(1L)).thenReturn(reclamo);

        mockMvc.perform(get("/api/v1/reclamo/obtener/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.idReclamo").value(1))
            .andExpect(jsonPath("$.estadoReclamo").value("EN_REVISION"));
    }

    @Test
    void obtenerReclamo_noEncontrado() throws Exception{
        when(reclamoService.obtenerPorIdReclamo(99L))
            .thenThrow(new ResourceNotFoundException("Reclamo", 99L));

            mockMvc.perform(get("/api/v1/reclamo/obtener/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void registrarReclamo_ok() throws Exception{
        ReclamoDTO dto = new ReclamoDTO();
        dto.setIdPedido(10L);
        dto.setIdProducto(5L);
        dto.setMotivo("Producto dañado");
        dto.setDescripcion("El producto llegó con la caja rota y el interior dañado");

        Reclamo creado = Reclamo.builder()
            .idReclamo(1L)
            .estadoReclamo("EN_REVISION")
            .build();

        when(reclamoService.registrarReclamo(eq(1L), any(ReclamoDTO.class))).thenReturn(creado);

        mockMvc.perform(post("/api/v1/reclamo/registrar/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idReclamo").value(1))
                .andExpect(jsonPath("$.estadoReclamo").value("EN_REVISION"));
    }

    @Test
    void actulizarEstado_estadoInvalido() throws Exception{
        when(reclamoService.actualizarEstado(1L, "INVALIDA"))
            .thenThrow(new BusinessException("Estado de reclamo: INVALIDA"));

        mockMvc.perform(put("/api/v1/reclamo/actualizar/1")
            .param("nuevoEstado", "INVALIDA"))
            .andExpect(status().isBadRequest());
    }
    
}
