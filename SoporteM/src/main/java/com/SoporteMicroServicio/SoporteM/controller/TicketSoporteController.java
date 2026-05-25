package com.SoporteMicroServicio.SoporteM.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.SoporteMicroServicio.SoporteM.dto.CambiarEstadoDTO;
import com.SoporteMicroServicio.SoporteM.dto.CrearTicketDTO;
import com.SoporteMicroServicio.SoporteM.model.TicketSoporte;
import com.SoporteMicroServicio.SoporteM.service.TicketSoporteService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("api/v1/ticketSoporte")
@RequiredArgsConstructor

public class TicketSoporteController {

    private final TicketSoporteService ticketSoporteService;

    @GetMapping("/listar")
    public ResponseEntity<List<TicketSoporte>> listarTodosTickets() {
        log.debug("Iniciando metodo");
        return ResponseEntity.ok(ticketSoporteService.listarTodosLosTickets());
    }

    @GetMapping("/listar/{id}")
    public ResponseEntity<TicketSoporte> listarTicketPorId(@PathVariable Long id) {
        log.debug("Iniciando metodo");
        return ResponseEntity.ok(ticketSoporteService.obtenerTicketPorId(id));
    }

    @GetMapping("/cliente/{rutCliente}")
    public ResponseEntity<List<TicketSoporte>> listarPorCliente(@PathVariable Long rutCliente) {
        return ResponseEntity.ok(ticketSoporteService.listarPorCliente(rutCliente));
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<TicketSoporte>> listarPorEstado(@PathVariable String estado) {
        return ResponseEntity.ok(ticketSoporteService.listarPorEstado(estado));
    }

    @PostMapping("/crear")
    public ResponseEntity<TicketSoporte> crearTicket(@Valid @RequestBody CrearTicketDTO dto) {
        log.info("Iniciando el metodo");
        TicketSoporte creado = ticketSoporteService.crearTicket(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @PutMapping("/clasificar/{idTicket}")
    public ResponseEntity<TicketSoporte> clasificarSolicitud(
            @PathVariable Long idTicket,
            @RequestBody Map<String, Object> body) {

        String prioridad = (String) body.get("prioridad");
        Long idPersonal = body.get("idPersonal") != null
            ? ((Number) body.get("idPersonal")).longValue()
            : null;

        return ResponseEntity.ok(ticketSoporteService.clasificarSolicitud(idTicket, prioridad, idPersonal));
    }

    @PutMapping("/cambiarEstado/{idTicket}")
    public ResponseEntity<TicketSoporte> cambiarEstado(
            @PathVariable Long idTicket,
            @Valid @RequestBody CambiarEstadoDTO dto) {
        return ResponseEntity.ok(ticketSoporteService.cambiarEstado(idTicket, dto));
    }

    @PutMapping("/cerrar/{idTicket}")
    public ResponseEntity<TicketSoporte> cerrarTicket(
            @PathVariable Long idTicket,
            @RequestParam String usuarioResponsable) {
        return ResponseEntity.ok(ticketSoporteService.cerrarTicket(idTicket, usuarioResponsable));
    }

    @DeleteMapping("/eliminar/{idTicket}")
    public ResponseEntity<Void> eliminarTicket(@PathVariable Long idTicket) {
        ticketSoporteService.eliminarTicketSoporte(idTicket);
        return ResponseEntity.noContent().build();
    }

}
