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

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/v1/personalSoporte")
@RequiredArgsConstructor

public class PersonalSoporteController {

    private final PersonalSoporteService personalSoporteService;

    @GetMapping("/listar")
    public ResponseEntity<List<PersonalSoporte>> listarTodos() {
        return ResponseEntity.ok(personalSoporteService.listarTodos());
    }

    @GetMapping("/obtener/{rutPersonal}")
    public ResponseEntity<PersonalSoporte> obtenerPersonalPorRut(@PathVariable Long rutPersonal) {
        return ResponseEntity.ok(personalSoporteService.obtenerPorIdPersonal(rutPersonal));
    }

    @PostMapping("/crear")
    public ResponseEntity<PersonalSoporte> crear(@Valid @RequestBody PersonalSoporteDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(personalSoporteService.crearPersonal(dto));
    }

    @PutMapping("/actualizar/{rutPersonal}")
    public ResponseEntity<PersonalSoporte> actualizarPersonal(@PathVariable Long rutPersonal, @Valid @RequestBody PersonalSoporteDTO dto) {
        return ResponseEntity.ok(personalSoporteService.actualizarPersonal(rutPersonal, dto));
    }

    @DeleteMapping("/eliminar/{rutPersonal}")
    public ResponseEntity<Void> eliminarPersonal(@PathVariable Long rutPersonal) {
        personalSoporteService.eliminarPersonal(rutPersonal);
        return ResponseEntity.noContent().build();
    }

}
