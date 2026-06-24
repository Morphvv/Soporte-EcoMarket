package com.SoporteMicroServicio.SoporteM.repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import com.SoporteMicroServicio.SoporteM.model.SolicitudDevolucion;
import com.SoporteMicroServicio.SoporteM.model.TicketSoporte;

@DataJpaTest
@ActiveProfiles("test")
class SolicitudDevolucionRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private SolicitudDevolucionRepository solicitudDevolucionRepository;

    private TicketSoporte crearTicket() {
        return entityManager.persistAndFlush(TicketSoporte.builder()
                .runCliente(12345678L).asunto("Prueba").descripcion("Desc")
                .tipoSolicitud("RECLAMO").canal("WEB").prioridad("ALTA")
                .estadoTicket("ABIERTO").fechaCreacion(LocalDateTime.now()).build());
    }

    private SolicitudDevolucion crearSolicitud(TicketSoporte ticket, String estado) {
        return entityManager.persistAndFlush(SolicitudDevolucion.builder()
                .idPedido(10L).idProducto(5L).cantidad(2)
                .motivo("Producto defectuoso recibido")
                .estadoSolicitud(estado)
                .fechaSolicitud(LocalDateTime.now())
                .ticketSoporte(ticket).build());
    }

    @Test
    void guardarYBuscar_porId(){
        TicketSoporte ticket = crearTicket();
        SolicitudDevolucion guardada = crearSolicitud(ticket, "PENDIENTE");

        Optional<SolicitudDevolucion> resultado = solicitudDevolucionRepository
            .findById(guardada.getIdSolicitudD());

    assertTrue(resultado.isPresent());
    assertEquals("PENDIENTE", resultado.get().getEstadoSolicitud());
    }

    @Test
    void buscarTicket_porId(){
        TicketSoporte ticket = crearTicket();
        crearSolicitud(ticket, "PENDIENTE");

        Optional<SolicitudDevolucion> resultado = solicitudDevolucionRepository
            .findByTicketSoporteIdTicket(ticket.getIdTicket());

        assertTrue(resultado.isPresent());
    }

    @Test
    void buscarTicket_noExiste(){
        Optional<SolicitudDevolucion> resultado = solicitudDevolucionRepository
            .findByTicketSoporteIdTicket(999L);

        assertFalse(resultado.isPresent());
    }

    @Test
    void buscarPorEstado_solicitud(){
        TicketSoporte t1 = crearTicket();
        TicketSoporte t2 = entityManager.persistAndFlush(TicketSoporte.builder()
                .runCliente(99999999L).asunto("Otro").descripcion("Desc2")
                .tipoSolicitud("RECLAMO").canal("WEB").prioridad("MEDIA")
                .estadoTicket("ABIERTO").fechaCreacion(LocalDateTime.now()).build());

        crearSolicitud(t1, "PENDIENTE");
        crearSolicitud(t2, "APROBADA");

        List<SolicitudDevolucion> pendientes= solicitudDevolucionRepository
            .findByEstadoSolicitud("PENDIENTE");

        assertEquals(1, pendientes.size());
        assertEquals("PENDIENTE", pendientes.get(0).getEstadoSolicitud());
    }
    
}
