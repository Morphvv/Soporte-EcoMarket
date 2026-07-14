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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("api/v1/solicitudDevolucion")
@RequiredArgsConstructor
@Tag(name = "Solicitudes de Devolución", description = "Gestión de solicitudes de devolución de productos")

public class SolicitudDevolucionController {

    private final SolicitudDevolucionService solicitudDevolucionService;

    @Operation(summary = "Listar todas las solicitudes de devolución")
    @ApiResponse(responseCode = "200", description = "Lista retornada exitosamente")
    @GetMapping("/listar")
    public ResponseEntity<List<SolicitudDevolucion>> listarTodasSolicitudes() {
        return ResponseEntity.ok(solicitudDevolucionService.listarTodos());
    }

    @Operation(summary = "Obtener una solicitud de devolución por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Solicitud encontrada"),
        @ApiResponse(responseCode = "404", description = "Solicitud no existe")
    })
    @GetMapping("/listar/{idSolicitud}")
    public ResponseEntity<SolicitudDevolucion> listarPorId(@PathVariable Long idSolicitud) {
        return ResponseEntity.ok(solicitudDevolucionService.obtenerPorIdDevolucion(idSolicitud));
    }

    @Operation(summary = "Registrar una solicitud de devolución para un ticket")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Solicitud registrada"),
        @ApiResponse(responseCode = "400", description = "El ticket ya tiene una solicitud de devolución"),
        @ApiResponse(responseCode = "404", description = "Ticket no encontrado")
    })
    @PostMapping("/registrar/{idTicket}")
    public ResponseEntity<SolicitudDevolucion> registrarSolicitud(@PathVariable Long idTicket, @Valid @RequestBody SolicitudDevolucionDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(solicitudDevolucionService.registrarSolicitud(idTicket, dto));
    }

    @Operation(summary = "Validar los datos del producto de una solicitud")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Resultado de la validación"),
        @ApiResponse(responseCode = "404", description = "Solicitud no encontrada")
    })
    @GetMapping("/validar/{idSolicitud}")
    public ResponseEntity<Map<String, Boolean>> validarProducto(@PathVariable Long idSolicitud) {
        boolean valido = solicitudDevolucionService.validarProducto(idSolicitud);
        return ResponseEntity.ok(Map.of("valido", valido));
    }

    @Operation(summary = "Aprobar una solicitud de devolución")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Solicitud aprobada"),
        @ApiResponse(responseCode = "400", description = "Datos del producto no válidos"),
        @ApiResponse(responseCode = "404", description = "Solicitud no encontrada")
    })
    @PutMapping("/aprobar/{idSolicitud}")
    public ResponseEntity<SolicitudDevolucion> aprobarDevolucion(@PathVariable Long idSolicitud) {
        return ResponseEntity.ok(solicitudDevolucionService.aprobarSolicitud(idSolicitud));
    }

    @Operation(summary = "Rechazar una solicitud de devolución")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Solicitud rechazada"),
        @ApiResponse(responseCode = "400", description = "La solicitud no está pendiente"),
        @ApiResponse(responseCode = "404", description = "Solicitud no encontrada")
    })
    @PutMapping("/rechazar/{idSolicitud}")
    public ResponseEntity<SolicitudDevolucion> rechazarDevolucion(@PathVariable Long idSolicitud) {
        return ResponseEntity.ok(solicitudDevolucionService.rechazarDevolucion(idSolicitud));
    }

}
