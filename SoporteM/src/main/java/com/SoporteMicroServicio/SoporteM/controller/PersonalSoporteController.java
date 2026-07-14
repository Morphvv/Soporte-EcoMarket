package com.SoporteMicroServicio.SoporteM.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.SoporteMicroServicio.SoporteM.dto.PersonalSoporteDTO;
import com.SoporteMicroServicio.SoporteM.model.PersonalSoporte;
import com.SoporteMicroServicio.SoporteM.service.PersonalSoporteService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/v1/personalSoporte")
@RequiredArgsConstructor
@Tag(name = "Personal de Soporte", description = "Gestión del personal del área de soporte")

public class PersonalSoporteController {

    private final PersonalSoporteService personalSoporteService;

    @Operation(summary = "Listar todo el personal de soporte")
    @ApiResponse(responseCode = "200", description = "Lista retornada exitosamente")
    @GetMapping("/listar")
    public ResponseEntity<List<PersonalSoporte>> listarTodos() {
        return ResponseEntity.ok(personalSoporteService.listarTodos());
    }

    @Operation(summary = "Obtener personal de soporte por RUT")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Personal encontrado"),
        @ApiResponse(responseCode = "404", description = "Personal no existe")
    })
    @GetMapping("/obtener/{rutPersonal}")
    public ResponseEntity<PersonalSoporte> obtenerPersonalPorRut(@PathVariable Long rutPersonal) {
        return ResponseEntity.ok(personalSoporteService.obtenerPorIdPersonal(rutPersonal));
    }

    @Operation(summary = "Crear personal de soporte")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Personal creado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos o email ya registrado")
    })
    @PostMapping("/crear")
    public ResponseEntity<PersonalSoporte> crear(@Valid @RequestBody PersonalSoporteDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(personalSoporteService.crearPersonal(dto));
    }

    @Operation(summary = "Actualizar personal de soporte")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Personal actualizado"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "404", description = "Personal no encontrado")
    })
    @PutMapping("/actualizar/{rutPersonal}")
    public ResponseEntity<PersonalSoporte> actualizarPersonal(@PathVariable Long rutPersonal, @Valid @RequestBody PersonalSoporteDTO dto) {
        return ResponseEntity.ok(personalSoporteService.actualizarPersonal(rutPersonal, dto));
    }

    @Operation(summary = "Eliminar personal de soporte por RUT")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Personal eliminado"),
        @ApiResponse(responseCode = "404", description = "Personal no encontrado")
    })
    @DeleteMapping("/eliminar/{rutPersonal}")
    public ResponseEntity<Void> eliminarPersonal(@PathVariable Long rutPersonal) {
        personalSoporteService.eliminarPersonal(rutPersonal);
        return ResponseEntity.noContent().build();
    }

}
