package com.SoporteMicroServicio.SoporteM.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.SoporteMicroServicio.SoporteM.dto.EvidenciaAdjuntaDTO;
import com.SoporteMicroServicio.SoporteM.model.EvidenciaAdjunta;
import com.SoporteMicroServicio.SoporteM.service.EvidenciaAdjuntaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/v1/evidenciaAdjunta")
@RequiredArgsConstructor
@Tag(name = "Evidencias Adjuntas", description = "Gestión de archivos adjuntos en tickets de soporte")

public class EvidenciaAdjuntaController {

    private final EvidenciaAdjuntaService evidenciaAdjuntaService;

    @Operation(summary = "Listar evidencias adjuntas de un ticket")
    @ApiResponse(responseCode = "200", description = "Lista retornada exitosamente")
    @GetMapping("/listar/{idTicket}")
    public ResponseEntity<List<EvidenciaAdjunta>> listarPorIdTicket(@PathVariable Long idTicket) {
        return ResponseEntity.ok(evidenciaAdjuntaService.listarPorIdTicket(idTicket));
    }

    @Operation(summary = "Obtener una evidencia por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Evidencia encontrada"),
        @ApiResponse(responseCode = "404", description = "Evidencia no existe")
    })
    @GetMapping("/obtener/{idEvidencia}")
    public ResponseEntity<EvidenciaAdjunta> obtenerEvidenciaPorId(@PathVariable Long idEvidencia) {
        return ResponseEntity.ok(evidenciaAdjuntaService.obtenerEvidenciaPorId(idEvidencia));
    }

    @Operation(summary = "Adjuntar una evidencia a un ticket")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Evidencia adjuntada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "404", description = "Ticket no encontrado")
    })
    @PostMapping("/adjuntar/{idTicket}")
    public ResponseEntity<EvidenciaAdjunta> adjuntarEvidencia(@PathVariable Long idTicket, @Valid @RequestBody EvidenciaAdjuntaDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(evidenciaAdjuntaService.adjuntarEvidencia(idTicket, dto));
    }

    @Operation(summary = "Eliminar una evidencia por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Evidencia eliminada"),
        @ApiResponse(responseCode = "404", description = "Evidencia no encontrada")
    })
    @DeleteMapping("/eliminar/{idEvidencia}")
    public ResponseEntity<Void> eliminarEvidencia(@PathVariable Long idEvidencia) {
        evidenciaAdjuntaService.eliminarEvidencia(idEvidencia);
        return ResponseEntity.noContent().build();
    }

}
