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

import com.SoporteMicroServicio.SoporteM.dto.MensajeSoporteDTO;
import com.SoporteMicroServicio.SoporteM.model.MensajeSoporte;
import com.SoporteMicroServicio.SoporteM.service.MensajeSoporteService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("api/v1/mensajeSoporte")
@RequiredArgsConstructor
@Tag(name = "Mensajes de Soporte", description = "Mensajes e intercambio de información dentro de un ticket")

public class MensajeSoporteController {

    private final MensajeSoporteService mensajeSoporteService;

    @Operation(summary = "Listar mensajes de un ticket")
    @ApiResponse(responseCode = "200", description = "Lista retornada exitosamente")
    @GetMapping("/listar/{idTicket}")
    public ResponseEntity<List<MensajeSoporte>> listarPorIdTicket(@PathVariable Long idTicket) {
        return ResponseEntity.ok(mensajeSoporteService.listarPorIdTicket(idTicket));
    }

    @Operation(summary = "Obtener un mensaje por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Mensaje encontrado"),
        @ApiResponse(responseCode = "404", description = "Mensaje no existe")
    })
    @GetMapping("/{idMensaje}")
    public ResponseEntity<MensajeSoporte> obtenerMensajePorId(@PathVariable Long idMensaje) {
        return ResponseEntity.ok(mensajeSoporteService.obtenerMensajePorId(idMensaje));
    }

    @Operation(summary = "Enviar un mensaje a un ticket")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Mensaje enviado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos o ticket cerrado"),
        @ApiResponse(responseCode = "404", description = "Ticket no encontrado")
    })
    @PostMapping("/enviar/{idTicket}")
    public ResponseEntity<MensajeSoporte> enviarMensaje(@PathVariable Long idTicket, @Valid @RequestBody MensajeSoporteDTO dto) {
        log.info("Iniciando el metodo");
        MensajeSoporte mensajeCreado = mensajeSoporteService.enviarMensaje(idTicket, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(mensajeCreado);
    }

    @Operation(summary = "Responder un mensaje existente")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Respuesta enviada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos o ticket cerrado"),
        @ApiResponse(responseCode = "404", description = "Mensaje original no encontrado")
    })
    @PostMapping("/responder/{idMensaje}")
    public ResponseEntity<MensajeSoporte> responderMensaje(@PathVariable Long idMensaje, @Valid @RequestBody MensajeSoporteDTO dto) {
        return ResponseEntity.ok(mensajeSoporteService.responderMensaje(idMensaje, dto));
    }

    @Operation(summary = "Eliminar un mensaje por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Mensaje eliminado"),
        @ApiResponse(responseCode = "404", description = "Mensaje no encontrado")
    })
    @DeleteMapping("/eliminar/{idMensaje}")
    public ResponseEntity<Void> eliminarMensaje(@PathVariable Long idMensaje) {
        mensajeSoporteService.eliminarMensaje(idMensaje);
        return ResponseEntity.noContent().build();
    }

}
