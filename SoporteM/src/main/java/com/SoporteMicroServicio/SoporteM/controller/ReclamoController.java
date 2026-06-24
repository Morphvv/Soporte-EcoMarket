package com.SoporteMicroServicio.SoporteM.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.SoporteMicroServicio.SoporteM.dto.ReclamoDTO;
import com.SoporteMicroServicio.SoporteM.model.Reclamo;
import com.SoporteMicroServicio.SoporteM.service.ReclamoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/v1/reclamo")
@RequiredArgsConstructor
@Tag(name = "Reclamos", description = "Gestión de reclamos asociados a tickets de soporte")

public class ReclamoController {

    private final ReclamoService reclamoService;

    @GetMapping("/listar")
    public ResponseEntity<List<Reclamo>> listarTodos() {
        return ResponseEntity.ok(reclamoService.listarTodosReclamos());
    }

    @GetMapping("/obtener/{idReclamo}")
    public ResponseEntity<Reclamo> obtenerReclamoPorIdReclamo(@PathVariable Long idReclamo) {
        return ResponseEntity.ok(reclamoService.obtenerPorIdReclamo(idReclamo));
    }

    @Operation(summary = "Registrar un reclamo para un ticket")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Reclamo registrado"),
        @ApiResponse(responseCode = "400", description = "El ticket ya tiene un reclamo"),
        @ApiResponse(responseCode = "404", description = "Ticket no encontrado")
    })
    @PostMapping("/registrar/{idTicket}")
    public ResponseEntity<Reclamo> registrarReclamo(@PathVariable Long idTicket, @Valid @RequestBody ReclamoDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(reclamoService.registrarReclamo(idTicket, dto));
    }

    @Operation(summary = "Marcar un reclamo como revisado")
    @PutMapping("/revisar/{idReclamo}")
    public ResponseEntity<Reclamo> revisarReclamo(@PathVariable Long idReclamo) {
        return ResponseEntity.ok(reclamoService.revisarReclamo(idReclamo));
    }

    @Operation(summary = "Actualizar el estado de un reclamo")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Estado actualizado"),
        @ApiResponse(responseCode = "400", description = "Estado inválido")
    })
    @PutMapping("/actualizar/{idReclamo}")
    public ResponseEntity<Reclamo> actualizarEstado(@PathVariable Long idReclamo, @RequestParam String nuevoEstado) {
        return ResponseEntity.ok(reclamoService.actualizarEstado(idReclamo, nuevoEstado));
    }

}
