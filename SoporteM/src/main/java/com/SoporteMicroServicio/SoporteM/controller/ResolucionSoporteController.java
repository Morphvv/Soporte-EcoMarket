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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("api/v1/resolucionSoporte")
@RequiredArgsConstructor
@Tag(name = "Resoluciones de Soporte", description = "Registro y modificación de resoluciones de tickets")

public class ResolucionSoporteController {

    private final ResolucionSoporteService resolucionSoporteService;

    @Operation(summary = "Listar todas las resoluciones de soporte")
    @ApiResponse(responseCode = "200", description = "Lista retornada exitosamente")
    @GetMapping("/listar")
    public ResponseEntity<List<ResolucionSoporte>> listarTodas() {
        return ResponseEntity.ok(resolucionSoporteService.listarTodos());
    }

    @Operation(summary = "Obtener una resolución por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Resolución encontrada"),
        @ApiResponse(responseCode = "404", description = "Resolución no existe")
    })
    @GetMapping("/listar/{idResolucion}")
    public ResponseEntity<ResolucionSoporte> listarPorId(@PathVariable Long idResolucion) {
        return ResponseEntity.ok(resolucionSoporteService.obtenerPorIdResolucion(idResolucion));
    }

    @Operation(summary = "Registrar una resolución para un ticket")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Resolución registrada"),
        @ApiResponse(responseCode = "400", description = "El ticket ya tiene una resolución"),
        @ApiResponse(responseCode = "404", description = "Ticket no encontrado")
    })
    @PostMapping("/registrar/{idTicket}")
    public ResponseEntity<ResolucionSoporte> registrarResolucion(@PathVariable Long idTicket, @Valid @RequestBody ResolucionSoporteDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(resolucionSoporteService.registrarResolucion(idTicket, dto));
    }

    @Operation(summary = "Modificar una resolución existente")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Resolución modificada"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "404", description = "Resolución no encontrada")
    })
    @PutMapping("/modificar/{idResolucion}")
    public ResponseEntity<ResolucionSoporte> modificarResolucion(@PathVariable Long idResolucion, @Valid @RequestBody ResolucionSoporteDTO dto) {
        return ResponseEntity.ok(resolucionSoporteService.modificarResolucion(idResolucion, dto));
    }

}
