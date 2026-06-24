package com.SoporteMicroServicio.SoporteM.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import com.SoporteMicroServicio.SoporteM.model.PersonalSoporte;
import com.SoporteMicroServicio.SoporteM.model.TicketSoporte;

@DataJpaTest
@ActiveProfiles("test")
class TicketSoporteRepositoryTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private TicketSoporteRepository ticketSoporteRepository;

    private TicketSoporte crearTicket(Long runCliente, String estado, String prioridad){
        return testEntityManager.persistAndFlush(TicketSoporte.builder()
            .runCliente(runCliente)
            .asunto("Producto dañado")
            .descripcion("El producto llego roto")
            .tipoSolicitud("RECLAMO")
            .canal("WEB")
            .prioridad(prioridad)
            .estadoTicket(estado)
            .fechaCreacion(LocalDateTime.now())
            .build());
    }

    @Test
    void guardarYBuscarPorId(){
        TicketSoporte guardado = crearTicket(12345678L, "ABIERTO", "ALTA");

        Optional<TicketSoporte> resultado = ticketSoporteRepository.findById(guardado.getIdTicket());

        assertTrue(resultado.isPresent());
        assertEquals(12345678L, resultado.get().getRunCliente());
    }

    @Test
    void buscarTicketPor_RunCliente(){
        crearTicket(11111111L, "ABIERTO", "ALTA");
        crearTicket(22222222L, "ABIERTO", "MEDIA");
        crearTicket(33333333L, "CERRADO", "BAJA");

        List<TicketSoporte> abiertos = ticketSoporteRepository.findByEstadoTicket("ABIERTO");

        assertEquals(2, abiertos.size());
    }

    @Test
    void buscarPorPrioridad(){
        crearTicket(11111111L, "ABIERTO", "ALTA");
        crearTicket(22222222L, "ABIERTO", "MEDIA");

        List<TicketSoporte> alta = ticketSoporteRepository.findByPrioridad("ALTA");

        assertEquals(1, alta.size());
        assertEquals("ALTA", alta.get(0).getPrioridad());
    }

    @Test
    void buscarPersonalAsignado_rutPersonal(){
        PersonalSoporte personal = testEntityManager.persistAndFlush(PersonalSoporte.builder()
                .rutPersonalS(12345678L).nombre("Ana").apellido("Lopez")
                .email("ana@eco.cl").rol("AGENTE").estado("ACTIVO").build());

        TicketSoporte ticket = testEntityManager.persistAndFlush(TicketSoporte.builder()
                .runCliente(11111111L).asunto("Prueba").descripcion("Desc")
                .tipoSolicitud("CONSULTA").canal("WEB").prioridad("ALTA")
                .estadoTicket("ABIERTO").fechaCreacion(LocalDateTime.now())
                .personalAsignado(personal).build());

        List<TicketSoporte> resultado = ticketSoporteRepository
            .findByPersonalAsignadoRutPersonalS(12345678L);

        assertEquals(1, resultado.size());
        assertEquals(ticket.getIdTicket(), resultado.get(0).getIdTicket());
    }
}
