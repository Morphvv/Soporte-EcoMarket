package com.SoporteMicroServicio.SoporteM.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.SoporteMicroServicio.SoporteM.dto.SolicitudDevolucionDTO;
import com.SoporteMicroServicio.SoporteM.model.SolicitudDevolucion;
import com.SoporteMicroServicio.SoporteM.service.SolicitudDevolucionService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("api/v1/solicitudDevolucion")
@RequiredArgsConstructor

public class SolicitudDevolucionController {

    private final SolicitudDevolucionService solicitudDevolucionService;

    @GetMapping("/listar")
    public ResponseEntity<List<SolicitudDevolucion>> listarTodasSolicitudes() {
        return ResponseEntity.ok(solicitudDevolucionService.listarTodos());
    }

    @GetMapping("/listar/{idSolicitud}")
    public ResponseEntity<SolicitudDevolucion> listarPorId(@PathVariable Long idSolicitud) {
        return ResponseEntity.ok(solicitudDevolucionService.obtenerPorIdDevolucion(idSolicitud));
    }

    @PostMapping("/registrar/{idTicket}")
    public ResponseEntity<SolicitudDevolucion> registrarSolicitud(@PathVariable Long idTicket, @Valid @RequestBody SolicitudDevolucionDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(solicitudDevolucionService.registrarSolicitud(idTicket, dto));
    }

    @GetMapping("/validar/{idSolicitud}")
    public ResponseEntity<Map<String, Boolean>> validarProducto(@PathVariable Long idSolicitud) {
        boolean valido = solicitudDevolucionService.validarProducto(idSolicitud);
        return ResponseEntity.ok(Map.of("valido", valido));
    }

    @PutMapping("/aprobar/{idSolicitud}")
    public ResponseEntity<SolicitudDevolucion> aprobarDevolucion(@PathVariable Long idSolicitud) {
        return ResponseEntity.ok(solicitudDevolucionService.aprobarSolicitud(idSolicitud));
    }

    @PutMapping("/rechazar/{idSolicitud}")
    public ResponseEntity<SolicitudDevolucion> rechazarDevolucion(@PathVariable Long idSolicitud) {
        return ResponseEntity.ok(solicitudDevolucionService.rechazarDevolucion(idSolicitud));
    }

}
