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

import com.SoporteMicroServicio.SoporteM.model.MensajeSoporte;
import com.SoporteMicroServicio.SoporteM.model.TicketSoporte;

@DataJpaTest
@ActiveProfiles("test")
class MensajeSoporteRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private MensajeSoporteRepository mensajeSoporteRepository;

    private TicketSoporte crearTicket(){
        return entityManager.persistAndFlush(TicketSoporte.builder()
                .runCliente(12345678L).asunto("Prueba").descripcion("Desc")
                .tipoSolicitud("RECLAMO").canal("WEB").prioridad("ALTA")
                .estadoTicket("ABIERTO").fechaCreacion(LocalDateTime.now()).build());
    }

    @Test
    void guardarYBuscar_porId(){
        TicketSoporte ticket = crearTicket();
        MensajeSoporte guardado = entityManager.persistAndFlush(MensajeSoporte.builder()
                .contenido("Hola, necesito ayuda").remitente("Juan")
                .tipoRemitente("CLIENTE").fechaEnvio(LocalDateTime.now())
                .ticketSoporte(ticket).build());
        
        Optional<MensajeSoporte> resultado = mensajeSoporteRepository.findById(guardado.getIdMensaje());

        assertTrue(resultado.isPresent());
        assertEquals("Hola, necesito ayuda", resultado.get().getContenido());
    }
 
    @Test
    void buscarTicket_porId(){
        TicketSoporte ticket = crearTicket();
        entityManager.persistAndFlush(MensajeSoporte.builder()
                .contenido("Mensaje 1").remitente("Juan").tipoRemitente("CLIENTE")
                .fechaEnvio(LocalDateTime.now()).ticketSoporte(ticket).build());
        entityManager.persistAndFlush(MensajeSoporte.builder()
                .contenido("Mensaje 2").remitente("Ana").tipoRemitente("PERSONAL_SOPORTE")
                .fechaEnvio(LocalDateTime.now()).ticketSoporte(ticket).build());

        List<MensajeSoporte> resultado = mensajeSoporteRepository
            .findByTicketSoporteIdTicket(ticket.getIdTicket());

        assertEquals(2, resultado.size());
    }
}
