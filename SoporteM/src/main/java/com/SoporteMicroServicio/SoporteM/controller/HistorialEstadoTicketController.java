package com.SoporteMicroServicio.SoporteM.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.SoporteMicroServicio.SoporteM.model.HistorialEstadoTicket;
import com.SoporteMicroServicio.SoporteM.service.HistorialEstadoTicketService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/v1/historialEstadoTicket")
@RequiredArgsConstructor
@Tag(name = "Historial de Estados", description = "Consulta del historial de cambios de estado de tickets")

public class HistorialEstadoTicketController {

    private final HistorialEstadoTicketService historialEstadoTicketService;

    @GetMapping("/listar/{idTicket}")
    public ResponseEntity<List<HistorialEstadoTicket>> listarPorIdTicket(@PathVariable Long idTicket) {
        return ResponseEntity.ok(historialEstadoTicketService.listarPorIdTicket(idTicket));
    }

    @GetMapping("/{idHistorial}")
    public ResponseEntity<HistorialEstadoTicket> obtenerHistorialPorId(@PathVariable Long idHistorial) {
        return ResponseEntity.ok(historialEstadoTicketService.obtenerHistorialPorId(idHistorial));
    }

}
