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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.SoporteMicroServicio.SoporteM.dto.EvidenciaAdjuntaDTO;
import com.SoporteMicroServicio.SoporteM.exception.ResourceNotFoundException;
import com.SoporteMicroServicio.SoporteM.model.EvidenciaAdjunta;
import com.SoporteMicroServicio.SoporteM.service.EvidenciaAdjuntaService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(EvidenciaAdjuntaController.class)
class EvidenciaAdjuntaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private EvidenciaAdjuntaService evidenciaAdjuntaService;

    @Test
    void listarPorIdTicket_ok() throws Exception{
        EvidenciaAdjunta e1 = EvidenciaAdjunta.builder()
                .idEvidencia(1L).nombreArchivo("foto.jpg").tipoArchivo("IMAGEN").build();
        EvidenciaAdjunta e2 = EvidenciaAdjunta.builder()
                .idEvidencia(2L).nombreArchivo("doc.pdf").tipoArchivo("PDF").build();

        when(evidenciaAdjuntaService.listarPorIdTicket(1L)).thenReturn(List.of(e1, e2));

        mockMvc.perform(get("/api/v1/evidenciaAdjunta/listar/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(2))
            .andExpect(jsonPath("[0].nombreArchivo").value("foto.jpg"));
    }

    @Test
    void obtenerEvidenciasPorId_ok() throws Exception{
        EvidenciaAdjunta evidencia = EvidenciaAdjunta.builder()
            .idEvidencia(1L).nombreArchivo("foto.jpg").tipoArchivo("IMAGEN").build();

            when(evidenciaAdjuntaService.obtenerEvidenciaPorId(1L)).thenReturn(evidencia);

            mockMvc.perform(get("/api/v1/evidenciaAdjunta/obtener/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.idEvidencia").value(1))
            .andExpect(jsonPath("$.nombreArchivo").value("foto.jpg"));
    }

    @Test
    void obtenerEvidenciaPorId_noEncontrado() throws Exception{
        when(evidenciaAdjuntaService.obtenerEvidenciaPorId(99L))
            .thenThrow(new ResourceNotFoundException("EvidenciaAdjunta", 99L));

            mockMvc.perform(get("/api/v1/evidenciaAdjunta/obtener/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void adjuntarEvidencia_ok() throws Exception{
        EvidenciaAdjuntaDTO dto = new EvidenciaAdjuntaDTO();
        dto.setNombreArchivo("comprobante.pdf");
        dto.setTipoArchivo("PDF");
        dto.setUrlArchivo("http://storage/comprobante.pdf");

        EvidenciaAdjunta creada = EvidenciaAdjunta.builder()
            .idEvidencia(1L).nombreArchivo("comprobante.pdf").tipoArchivo("PDF").build();

            when(evidenciaAdjuntaService.adjuntarEvidencia(eq(1L), any(EvidenciaAdjuntaDTO.class)))
                .thenReturn(creada);

            mockMvc.perform(post("/api/v1/evidenciaAdjunta/adjuntar/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idEvidencia").value(1))
                .andExpect(jsonPath("$.tipoArchivo").value("PDF"));
    }

    @Test
    void eliminarEvidencia_ok() throws Exception{
        mockMvc.perform(delete("/api/v1/evidenciaAdjunta/eliminar/1"))
            .andExpect(status().isNoContent());
    }
    
}
