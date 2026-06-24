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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.SoporteMicroServicio.SoporteM.dto.PersonalSoporteDTO;
import com.SoporteMicroServicio.SoporteM.exception.ResourceNotFoundException;
import com.SoporteMicroServicio.SoporteM.model.PersonalSoporte;
import com.SoporteMicroServicio.SoporteM.service.PersonalSoporteService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(PersonalSoporteController.class)
class PersonalSoporteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private PersonalSoporteService personalSoporteService;

    @Test
    void listarTodos_ok() throws Exception{
        PersonalSoporte p1 = PersonalSoporte.builder()
                .rutPersonalS(12345678L).nombre("Ana").apellido("Lopez")
                .rol("AGENTE").estado("ACTIVO").build();
        PersonalSoporte p2 = PersonalSoporte.builder()
                .rutPersonalS(87654321L).nombre("Pedro").apellido("Soto")
                .rol("SUPERVISOR").estado("ACTIVO").build();

        when(personalSoporteService.listarTodos()).thenReturn(List.of(p1, p2));

        mockMvc.perform(get("/api/v1/personalSoporte/listar"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(2))
            .andExpect(jsonPath("$[0].nombre").value("Ana"));
    }

    @Test
    void obtenerPersonalPorRut() throws Exception{
        PersonalSoporte personal = PersonalSoporte.builder()
                .rutPersonalS(12345678L).nombre("Ana").apellido("Lopez")
                .rol("AGENTE").estado("ACTIVO").build();

        when(personalSoporteService.obtenerPorIdPersonal(12345678L)).thenReturn(personal);

        mockMvc.perform(get("/api/v1/personalSoporte/obtener/12345678"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.rutPersonalS").value(12345678))
            .andExpect(jsonPath("$.nombre").value("Ana"));
    }

    @Test
    void obtenerPersonalPorRut_noEncontrado() throws Exception{
        when(personalSoporteService.obtenerPorIdPersonal(99L))
            .thenThrow(new ResourceNotFoundException("PersonalSoporte", 99L));

        mockMvc.perform(get("/api/v1/personalSoporte/obtener/99"))
            .andExpect(status().isNotFound());
    }

    @Test
    void crear_ok() throws Exception{
        PersonalSoporteDTO dto = new PersonalSoporteDTO();
        dto.setRut(12345678L);
        dto.setNombre("Ana");
        dto.setApellido("Lopez");
        dto.setEmail("ana@ecomarket.cl");
        dto.setRol("AGENTE");
        dto.setEstado("ACTIVO");

        PersonalSoporte creado = PersonalSoporte.builder()
            .rutPersonalS(12345678L).nombre("Ana").apellido("Lopez")
            .rol("AGENTE").estado("ACTIVO").build();

        when(personalSoporteService.crearPersonal(any(PersonalSoporteDTO.class))).thenReturn(creado);
        
        mockMvc.perform(post("/api/v1/personalSoporte/crear")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre").value("Ana"))
                .andExpect(jsonPath("$.rol").value("AGENTE"));
    }

    @Test
    void actualizarPersonal_ok() throws Exception{
        PersonalSoporteDTO dto = new PersonalSoporteDTO();
        dto.setRut(12345678L);
        dto.setNombre("Ana");
        dto.setApellido("Lopez");
        dto.setEmail("ana@ecomarket.cl");
        dto.setRol("SUPERVISOR");
        dto.setEstado("ACTIVO");

        PersonalSoporte actualizado = PersonalSoporte.builder()
            .rutPersonalS(12345678L).nombre("Ana").rol("SUPERVISOR").estado("ACTIVO").build();

        when(personalSoporteService.actualizarPersonal(eq(12345678L), any(PersonalSoporteDTO.class)))
            .thenReturn(actualizado);

        mockMvc.perform(put("/api/v1/personalSoporte/actualizar/12345678")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.rol").value("SUPERVISOR"));
    }

    @Test
    void eliminarPersonal_ok() throws Exception{
        doThrow(new ResourceNotFoundException("PersonalSoporte", 99L))
            .when(personalSoporteService).eliminarPersonal(99L);

        mockMvc.perform(delete("/api/v1/personalSoporte/eliminar/99"))
            .andExpect(status().isNotFound());
    }

    @Test
    void eliminarPersonal_exitoso() throws Exception {
        mockMvc.perform(delete("/api/v1/personalSoporte/eliminar/12345678"))
            .andExpect(status().isNoContent());
    }
}
