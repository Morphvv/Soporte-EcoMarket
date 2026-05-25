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
import org.springframework.web.bind.annotation.RestController;

import com.SoporteMicroServicio.SoporteM.dto.ResolucionSoporteDTO;
import com.SoporteMicroServicio.SoporteM.model.ResolucionSoporte;
import com.SoporteMicroServicio.SoporteM.service.ResolucionSoporteService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("api/v1/resolucionSoporte")
@RequiredArgsConstructor

public class ResolucionSoporteController {

    private final ResolucionSoporteService resolucionSoporteService;

    @GetMapping("/listar")
    public ResponseEntity<List<ResolucionSoporte>> listarTodas() {
        return ResponseEntity.ok(resolucionSoporteService.listarTodos());
    }

    @GetMapping("/listar/{idResolucion}")
    public ResponseEntity<ResolucionSoporte> listarPorId(@PathVariable Long idResolucion) {
        return ResponseEntity.ok(resolucionSoporteService.obtenerPorIdResolucion(idResolucion));
    }

    @PostMapping("/registrar/{idTicket}")
    public ResponseEntity<ResolucionSoporte> registrarResolucion(@PathVariable Long idTicket, @Valid @RequestBody ResolucionSoporteDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(resolucionSoporteService.registrarResolucion(idTicket, dto));
    }

    @PutMapping("/modificar/{idResolucion}")
    public ResponseEntity<ResolucionSoporte> modificarResolucion(@PathVariable Long idResolucion, @Valid @RequestBody ResolucionSoporteDTO dto) {
        return ResponseEntity.ok(resolucionSoporteService.modificarResolucion(idResolucion, dto));
    }

}
