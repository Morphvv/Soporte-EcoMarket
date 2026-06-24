package com.SoporteMicroServicio.SoporteM.repository;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import com.SoporteMicroServicio.SoporteM.model.PersonalSoporte;

@DataJpaTest
@ActiveProfiles("test")
class PersonalSoporteRepositoryTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private PersonalSoporteRepository personalSoporteRepository;

    private PersonalSoporte crearPersonal(Long rut, String email, String rol, String estado){
        return testEntityManager.persistAndFlush(PersonalSoporte.builder()
            .rutPersonalS(rut)
            .nombre("Ana")
            .apellido("Lopez")
            .email(email)
            .rol(rol)
            .estado(estado)
            .build());
    }

    @Test
    void guardarYBuscarPorId() {
        PersonalSoporte guaradado= crearPersonal(12345678L, "ana@gmail.com", "AGENTE", "ACTIVO");

        Optional<PersonalSoporte> resultado = personalSoporteRepository.findById(12345678L);

        assertTrue(resultado.isPresent());
        assertEquals("Ana", resultado.get().getNombre());
    }
    
    @Test
    void buscarEmail(){
        crearPersonal(12345678L, "ana@gmail.com", "AGENTE", "ACTIVO");

        Optional<PersonalSoporte> resultado = personalSoporteRepository.findByEmail("ana@gmail.com");

        assertTrue(resultado.isPresent());
        assertEquals(12345678L, resultado.get().getRutPersonalS());
    }

    @Test
    void buscarEmail_noEncontrado() {
        Optional<PersonalSoporte> resultado = personalSoporteRepository.findByEmail("noexiste@gmail.com");

        assertFalse(resultado.isPresent());
    }

    @Test
    void buscarEstado_activos(){
        crearPersonal(11111111L, "uno@eco.cl", "AGENTE", "ACTIVO");
        crearPersonal(22222222L, "dos@eco.cl", "AGENTE", "INACTIVO");

        List<PersonalSoporte> activos = personalSoporteRepository.findByEstado("ACTIVO");

        assertEquals(1, activos.size());
        assertEquals("ACTIVO", activos.get(0).getEstado());
    }

    @Test
    void buscarRol(){
        crearPersonal(11111111L, "uno@eco.cl", "AGENTE", "ACTIVO");
        crearPersonal(22222222L, "dos@eco.cl", "SUPERVISOR", "ACTIVO");

        List<PersonalSoporte> agentes = personalSoporteRepository.findByRol("AGENTE");

        assertEquals(1, agentes.size());
        assertEquals("AGENTE", agentes.get(0).getRol());
    }
}
