package com.SoporteMicroServicio.SoporteM.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.SoporteMicroServicio.SoporteM.model.HistorialEstadoTicket;
import com.SoporteMicroServicio.SoporteM.service.HistorialEstadoTicketService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/v1/historialEstadoTicket")
@RequiredArgsConstructor
@Tag(name = "Historial de Estados", description = "Consulta del historial de cambios de estado de tickets")

public class HistorialEstadoTicketController {

    private final HistorialEstadoTicketService historialEstadoTicketService;

    @Operation(summary = "Listar el historial de estados de un ticket")
    @ApiResponse(responseCode = "200", description = "Historial retornado exitosamente")
    @GetMapping("/listar/{idTicket}")
    public ResponseEntity<List<HistorialEstadoTicket>> listarPorIdTicket(@PathVariable Long idTicket) {
        return ResponseEntity.ok(historialEstadoTicketService.listarPorIdTicket(idTicket));
    }

    @Operation(summary = "Obtener un registro de historial por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Registro encontrado"),
        @ApiResponse(responseCode = "404", description = "Registro no existe")
    })
    @GetMapping("/{idHistorial}")
    public ResponseEntity<HistorialEstadoTicket> obtenerHistorialPorId(@PathVariable Long idHistorial) {
        return ResponseEntity.ok(historialEstadoTicketService.obtenerHistorialPorId(idHistorial));
    }

}
