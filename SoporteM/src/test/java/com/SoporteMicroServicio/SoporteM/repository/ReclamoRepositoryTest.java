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

import com.SoporteMicroServicio.SoporteM.model.Reclamo;
import com.SoporteMicroServicio.SoporteM.model.TicketSoporte;

@DataJpaTest
@ActiveProfiles("test")
class ReclamoRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ReclamoRepository reclamoRepository;

    
    private TicketSoporte crearTicket() {
        return entityManager.persistAndFlush(TicketSoporte.builder()
                .runCliente(12345678L).asunto("Prueba").descripcion("Desc")
                .tipoSolicitud("RECLAMO").canal("WEB").prioridad("ALTA")
                .estadoTicket("ABIERTO").fechaCreacion(LocalDateTime.now()).build());
    }

    private Reclamo crearReclamo(TicketSoporte ticket, String estado){
        return entityManager.persistAndFlush(Reclamo.builder()
            .idPedido(10L).idProducto(5L)
            .motivo("Producto defectuoso")
            .descripcion("El producto llego roto")
            .estadoReclamo(estado)
            .fechaReclamo(LocalDateTime.now())
            .ticketSoporte(ticket).build());
    }

    @Test
    void guardarYbuscar_porId(){
        TicketSoporte ticket = crearTicket();
        Reclamo guardado = crearReclamo(ticket, "EN_REVISION");

        Optional<Reclamo> resultado = reclamoRepository.findById(guardado.getIdReclamo());

        assertTrue(resultado.isPresent());
        assertEquals("EN_REVISION", resultado.get().getEstadoReclamo());
    }

    @Test
    void buscarTicket_porId(){
        TicketSoporte ticket = crearTicket();
        crearReclamo(ticket, "EN_REVISION");

        Optional<Reclamo> resultado = reclamoRepository.findByTicketSoporteIdTicket(ticket.getIdTicket());

        assertTrue(resultado.isPresent());
    }

    @Test
    void buscarTicket_noExiste(){
        Optional<Reclamo> resultado = reclamoRepository.findByTicketSoporteIdTicket(999L);

        assertFalse(resultado.isPresent());
    }

    @Test
    void buscarEstadoReclamo(){
        TicketSoporte t1 = crearTicket();
        TicketSoporte t2 = entityManager.persistAndFlush(TicketSoporte.builder()
                .runCliente(99999999L).asunto("Otro").descripcion("Desc2")
                .tipoSolicitud("RECLAMO").canal("WEB").prioridad("MEDIA")
                .estadoTicket("ABIERTO").fechaCreacion(LocalDateTime.now()).build());

        crearReclamo(t1, "EN_REVISION");
        crearReclamo(t2, "RESUELTO");

        List<Reclamo> enRevision = reclamoRepository.findByEstadoReclamo("EN_REVISION");

        assertEquals(1, enRevision.size());
        assertEquals("EN_REVISION", enRevision.get(0).getEstadoReclamo());
    }
}
