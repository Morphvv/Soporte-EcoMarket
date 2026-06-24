package com.SoporteMicroServicio.SoporteM.repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import com.SoporteMicroServicio.SoporteM.model.EvidenciaAdjunta;
import com.SoporteMicroServicio.SoporteM.model.TicketSoporte;

@DataJpaTest
@ActiveProfiles("test")
class EvidenciaAdjuntaRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private EvidenciaAdjuntaRepository evidenciaAdjuntaRepository;

    private TicketSoporte crearTicket(){
        return entityManager.persistAndFlush(TicketSoporte.builder()
            .runCliente(12345678L).asunto("Prueba").descripcion("Desc")
            .tipoSolicitud("RECLAMO").canal("WEB").prioridad("ALTA")
            .estadoTicket("ABIERTO").fechaCreacion(LocalDateTime.now()).build());
    }

    @Test
    void guardarYBuscarPorId(){
        TicketSoporte ticket = crearTicket();
        EvidenciaAdjunta guardada = entityManager.persistAndFlush(EvidenciaAdjunta.builder()
                .nombreArchivo("foto.jpg").tipoArchivo("IMAGEN")
                .urlArchivo("http://storage/foto.jpg")
                .fechaCarga(LocalDateTime.now()).ticketSoporte(ticket).build());
        
        Optional<EvidenciaAdjunta> resultado = evidenciaAdjuntaRepository.findById(guardada.getIdEvidencia());

        assertTrue(resultado.isPresent());
        assertEquals("foto.jpg", resultado.get().getNombreArchivo());
    }

    @Test
    void buscarTicket_porId(){
        TicketSoporte ticket = crearTicket();
        entityManager.persistAndFlush(EvidenciaAdjunta.builder()
                .nombreArchivo("foto.jpg").tipoArchivo("IMAGEN")
                .urlArchivo("http://storage/foto.jpg")
                .fechaCarga(LocalDateTime.now()).ticketSoporte(ticket).build());
        entityManager.persistAndFlush(EvidenciaAdjunta.builder()
                .nombreArchivo("doc.pdf").tipoArchivo("PDF")
                .urlArchivo("http://storage/doc.pdf")
                .fechaCarga(LocalDateTime.now()).ticketSoporte(ticket).build());

        List<EvidenciaAdjunta> resultado = evidenciaAdjuntaRepository.findByTicketSoporteIdTicket(ticket.getIdTicket());

        assertEquals(2, resultado.size());
    }
}
