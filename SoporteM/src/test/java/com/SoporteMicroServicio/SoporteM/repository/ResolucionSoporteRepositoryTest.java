package com.SoporteMicroServicio.SoporteM.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import com.SoporteMicroServicio.SoporteM.model.ResolucionSoporte;
import com.SoporteMicroServicio.SoporteM.model.TicketSoporte;

@DataJpaTest
@ActiveProfiles("test")
class ResolucionSoporteRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ResolucionSoporteRepository resolucionSoporteRepository;

    private TicketSoporte crearTicket() {
        return entityManager.persistAndFlush(TicketSoporte.builder()
                .runCliente(12345678L).asunto("Prueba").descripcion("Desc")
                .tipoSolicitud("RECLAMO").canal("WEB").prioridad("ALTA")
                .estadoTicket("ABIERTO").fechaCreacion(LocalDateTime.now()).build());
    }

    @Test
    void guardarYBuscar_porId(){
        TicketSoporte ticket = crearTicket();
        ResolucionSoporte guardada = entityManager.persistAndFlush(ResolucionSoporte.builder()
                .tipoResolucion("REEMBOLSO")
                .descripcion("Se realizara el reembolso al cliente")
                .aprobadoPor("supervisor")
                .fechaResolucion(LocalDateTime.now())
                .ticketSoporte(ticket).build());

        Optional<ResolucionSoporte> resultado = resolucionSoporteRepository.findById(guardada.getIdResolucion());

        assertTrue(resultado.isPresent());
        assertEquals("REEMBOLSO", resultado.get().getTipoResolucion());
    }

    @Test
    void buscarTicket_porId(){
        TicketSoporte ticket = crearTicket();
        entityManager.persistAndFlush(ResolucionSoporte.builder()
                .tipoResolucion("REEMBOLSO")
                .descripcion("Se realizara el reembolso al cliente")
                .aprobadoPor("supervisor")
                .fechaResolucion(LocalDateTime.now())
                .ticketSoporte(ticket).build());

        Optional<ResolucionSoporte> resultado = resolucionSoporteRepository
            .findByTicketSoporteIdTicket(ticket.getIdTicket());

        assertTrue(resultado.isPresent());
        assertEquals("REEMBOLSO", resultado.get().getTipoResolucion());
    }

    @Test
    void buscarTicketId_noExiste(){
        Optional<ResolucionSoporte> resultado = resolucionSoporteRepository
            .findByTicketSoporteIdTicket(999L);

        assertFalse(resultado.isPresent());
    }
    
}
