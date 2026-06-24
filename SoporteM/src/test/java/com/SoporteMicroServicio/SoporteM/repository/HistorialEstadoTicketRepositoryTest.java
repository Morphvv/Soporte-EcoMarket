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

import com.SoporteMicroServicio.SoporteM.model.HistorialEstadoTicket;
import com.SoporteMicroServicio.SoporteM.model.TicketSoporte;

@DataJpaTest
@ActiveProfiles("test")
class HistorialEstadoTicketRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private HistorialEstadoTicketRepository historialEstadoTicketRepository;

    private TicketSoporte crearTicket(){
        return entityManager.persistAndFlush(TicketSoporte.builder()
                .runCliente(12345678L).asunto("Prueba").descripcion("Desc")
                .tipoSolicitud("RECLAMO").canal("WEB").prioridad("ALTA")
                .estadoTicket("ABIERTO").fechaCreacion(LocalDateTime.now()).build());
    }

    @Test
    void guardarYBuscar_id(){
        TicketSoporte ticket = crearTicket();
        HistorialEstadoTicket guardado = entityManager.persistAndFlush(HistorialEstadoTicket.builder()
                .estadoAnterior("ABIERTO").estadoNuevo("EN_PROCESO")
                .fechaCambio(LocalDateTime.now()).usuarioResponsable("admin")
                .ticketSoporte(ticket).build());

        Optional<HistorialEstadoTicket> resultado = historialEstadoTicketRepository.findById(guardado.getIdHistorial());

        assertTrue(resultado.isPresent());
        assertEquals("EN_PROCESO", resultado.get().getEstadoNuevo());
    }

    @Test
    void bucarTicket_porFechasAsc(){
        TicketSoporte ticket = crearTicket();
        LocalDateTime primero = LocalDateTime.now().minusHours(2);
        LocalDateTime segundo = LocalDateTime.now();

        entityManager.persistAndFlush(HistorialEstadoTicket.builder()
            .estadoAnterior(null).estadoNuevo("ABIERTO")
            .fechaCambio(primero).usuarioResponsable("Sistema")
            .ticketSoporte(ticket).build());
        
        entityManager.persistAndFlush(HistorialEstadoTicket.builder()
            .estadoAnterior("ABIERTO").estadoNuevo("EN_PROCESO")
            .fechaCambio(segundo).usuarioResponsable("admin")
            .ticketSoporte(ticket).build());

        List<HistorialEstadoTicket> resultado = historialEstadoTicketRepository
            .findByTicketSoporteIdTicketOrderByFechaCambioAsc(ticket.getIdTicket());

        assertEquals(2, resultado.size());
        assertEquals("ABIERTO", resultado.get(0).getEstadoNuevo());
        assertEquals("EN_PROCESO", resultado.get(1).getEstadoNuevo());
    }
}
