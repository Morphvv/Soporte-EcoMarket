package com.SoporteMicroServicio.SoporteM.controller;
import java.util.List;

import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.SoporteMicroServicio.SoporteM.dto.MensajeSoporteDTO;
import com.SoporteMicroServicio.SoporteM.exception.ResourceNotFoundException;
import com.SoporteMicroServicio.SoporteM.model.MensajeSoporte;
import com.SoporteMicroServicio.SoporteM.service.MensajeSoporteService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(MensajeSoporteController.class)
class MensajeSoporteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private MensajeSoporteService mensajeSoporteService;

    @Test
    void listarPorIdTicket_ok() throws Exception{
        MensajeSoporte m1 = MensajeSoporte.builder()
                .idMensaje(1L).contenido("Hola").remitente("Juan").tipoRemitente("CLIENTE").build();
        MensajeSoporte m2 = MensajeSoporte.builder()
                .idMensaje(2L).contenido("En revision").remitente("Ana").tipoRemitente("PERSONAL_SOPORTE").build();

        when(mensajeSoporteService.listarPorIdTicket(1L)).thenReturn(List.of(m1, m2));

        mockMvc.perform(get("/api/v1/mensajeSoporte/listar/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(2))
            .andExpect(jsonPath("[0].tipoRemitente").value("CLIENTE"));
    }

    @Test
    void obtenerMensajePorId_ok() throws Exception{
        MensajeSoporte mensaje = MensajeSoporte.builder()
            .idMensaje(1L).contenido("Hola").remitente("Juan").tipoRemitente("CLIENTE").build();

            when(mensajeSoporteService.obtenerMensajePorId(1L)).thenReturn(mensaje);

            mockMvc.perform(get("/api/v1/mensajeSoporte/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idMensaje").value(1))
                .andExpect(jsonPath("$.contenido").value("Hola"));
    }

    @Test
    void obtenerMensajePorId_noEncontrado() throws Exception{
        when(mensajeSoporteService.obtenerMensajePorId(99L))
            .thenThrow(new ResourceNotFoundException("MensajeSoporte", 99L));

            mockMvc.perform(get("/api/v1/mensajeSoporte/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void enviarMensaje_ok() throws Exception{
        MensajeSoporteDTO dto = new MensajeSoporteDTO();
        dto.setContenido("El producto llegó roto");
        dto.setRemitente("Juan");
        dto.setTipoRemitente("CLIENTE");
        
        MensajeSoporte creado = MensajeSoporte.builder()
            .idMensaje(1L).contenido("El producto llego roto").tipoRemitente("CLIENTE").build();

            when(mensajeSoporteService.enviarMensaje(eq(1L), any(MensajeSoporteDTO.class))).thenReturn(creado);

            mockMvc.perform(post("/api/v1/mensajeSoporte/enviar/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idMensaje").value(1));
    }

    @Test
    void responderMensaje_ok() throws Exception{
        MensajeSoporteDTO dto = new MensajeSoporteDTO();
        dto.setContenido("Estamos revisando su caso");
        dto.setRemitente("Ana");
        dto.setTipoRemitente("PERSONAL_SOPORTE");
        
        MensajeSoporte respuesta = MensajeSoporte.builder()
            .idMensaje(2L).contenido("Estamos revisando su caso").tipoRemitente("PERSONAL_SOPORTE").build();

        when(mensajeSoporteService.responderMensaje(eq(1L), any(MensajeSoporteDTO.class))).thenReturn(respuesta);

        mockMvc.perform(post("/api/v1/mensajeSoporte/responder/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.tipoRemitente").value("PERSONAL_SOPORTE"));   
    }

    @Test
    void eliminarMensaje_ok() throws Exception{
        mockMvc.perform(delete("/api/v1/mensajeSoporte/eliminar/1"))
            .andExpect(status().isNoContent());
    }

    @Test
    void eliminarMensaje_noEncontrado() throws Exception{
        doThrow(new ResourceNotFoundException("MensajeSoporte", 99L))
                .when(mensajeSoporteService).eliminarMensaje(99L);

        mockMvc.perform(delete("/api/v1/mensajeSoporte/eliminar/99"))
                .andExpect(status().isNotFound());
    }
}
