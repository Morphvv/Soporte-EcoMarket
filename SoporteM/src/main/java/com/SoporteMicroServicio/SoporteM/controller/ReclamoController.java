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

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/v1/reclamo")
@RequiredArgsConstructor

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

    @PostMapping("/registrar/{idTicket}")
    public ResponseEntity<Reclamo> registrarReclamo(@PathVariable Long idTicket, @Valid @RequestBody ReclamoDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(reclamoService.registrarReclamo(idTicket, dto));
    }

    @PutMapping("/revisar/{idReclamo}")
    public ResponseEntity<Reclamo> revisarReclamo(@PathVariable Long idReclamo) {
        return ResponseEntity.ok(reclamoService.revisarReclamo(idReclamo));
    }

    @PutMapping("/actualizar/{idReclamo}")
    public ResponseEntity<Reclamo> actualizarEstado(@PathVariable Long idReclamo, @RequestParam String nuevoEstado) {
        return ResponseEntity.ok(reclamoService.actualizarEstado(idReclamo, nuevoEstado));
    }

}
