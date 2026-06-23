package com.SoporteMicroServicio.SoporteM.controller;
import java.util.List;

import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.SoporteMicroServicio.SoporteM.dto.ResolucionSoporteDTO;
import com.SoporteMicroServicio.SoporteM.exception.ResourceNotFoundException;
import com.SoporteMicroServicio.SoporteM.model.ResolucionSoporte;
import com.SoporteMicroServicio.SoporteM.service.ResolucionSoporteService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(ResolucionSoporteController.class)
class ResolucionSoporteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ResolucionSoporteService resolucionSoporteService;

    @Test
    void listarTodas_ok() throws Exception{
        ResolucionSoporte r1 = ResolucionSoporte.builder()
                .idResolucion(1L).tipoResolucion("REEMBOLSO").aprobadoPor("admin").build();
        ResolucionSoporte r2 = ResolucionSoporte.builder()
                .idResolucion(2L).tipoResolucion("REEMPLAZO").aprobadoPor("supervisor").build();

        when(resolucionSoporteService.listarTodos()).thenReturn(List.of(r1, r2));

        mockMvc.perform(get("/api/v1/resolucionSoporte/listar"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(2))
            .andExpect(jsonPath("$[0].tipoResolucion").value("REEMBOLSO"));
    }

    @Test
    void listarPorId_ok() throws Exception{
        ResolucionSoporte resolucion = ResolucionSoporte.builder()
                .idResolucion(1L).tipoResolucion("REEMBOLSO").aprobadoPor("admin").build();

                when(resolucionSoporteService.obtenerPorIdResolucion(1L)).thenReturn(resolucion);

                mockMvc.perform(get("/api/v1/resolucionSoporte/listar/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.idResolucion").value(1))
                    .andExpect(jsonPath("$.tipoResolucion").value("REEMBOLSO"));
    }

    @Test
    void listarPorId_noEncontrado() throws Exception{
        when(resolucionSoporteService.obtenerPorIdResolucion(99L))
            .thenThrow(new ResourceNotFoundException("ResolucionSoporte", 99L));

        mockMvc.perform(get("/api/v1/resolucionSoporte/listar/99"))
            .andExpect(status().isNotFound());
    }

    @Test
    void registrarResolucion_ok() throws Exception{
        ResolucionSoporteDTO dto = new ResolucionSoporteDTO();
        dto.setTipoResolucion("REEMBOLSO");
        dto.setDescripcion("Se realizara el reembolso completo al cliente");
        dto.setAprobadoPor("supervisor");

        ResolucionSoporte creada = ResolucionSoporte.builder()
            .idResolucion(1L).tipoResolucion("REEMBOLSO").aprobadoPor("supervisor").build();

        when(resolucionSoporteService.registrarResolucion(eq(1L), any(ResolucionSoporteDTO.class))).thenReturn(creada);

        mockMvc.perform(post("/api/v1/resolucionSoporte/registrar/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.idResolucion").value(1))
            .andExpect(jsonPath("$.tipoResolucion").value("REEMBOLSO"));
    }

    @Test
    void modificarResolucion_ok() throws Exception{
        ResolucionSoporteDTO dto = new ResolucionSoporteDTO();
        dto.setTipoResolucion("DEVOLUCION");
        dto.setDescripcion("Se cambia a devolucion del producto recibido");
        dto.setAprobadoPor("admin");

        ResolucionSoporte modificada = ResolucionSoporte.builder()
            .idResolucion(1L).tipoResolucion("DEVOLUCION").aprobadoPor("admin").build();

        when(resolucionSoporteService.modificarResolucion(eq(1L), any(ResolucionSoporteDTO.class))).thenReturn(modificada);

        mockMvc.perform(put("/api/v1/resolucionSoporte/modificar/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tipoResolucion").value("DEVOLUCION"));   
    }
}
