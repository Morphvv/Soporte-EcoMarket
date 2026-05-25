package com.SoporteMicroServicio.SoporteM.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.SoporteMicroServicio.SoporteM.dto.PersonalSoporteDTO;
import com.SoporteMicroServicio.SoporteM.exception.BusinessException;
import com.SoporteMicroServicio.SoporteM.exception.ResourceNotFoundException;
import com.SoporteMicroServicio.SoporteM.model.PersonalSoporte;
import com.SoporteMicroServicio.SoporteM.repository.PersonalSoporteRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor

public class PersonalSoporteService {

    private final PersonalSoporteRepository personalSoporteRepository;

    public List<PersonalSoporte> listarTodos() {
        return personalSoporteRepository.findAll();
    }

    public PersonalSoporte obtenerPorIdPersonal(Long idPersonal) {
        return personalSoporteRepository.findById(idPersonal)
            .orElseThrow(() -> new ResourceNotFoundException("Personal de soporte", idPersonal));
    }

    public PersonalSoporte crearPersonal(PersonalSoporteDTO dto) {
        log.info("Creando personal de soporte: {}", dto.getRut());

        if (personalSoporteRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new BusinessException("Ya existe personal registrado con ese email");
        }

        PersonalSoporte personal = PersonalSoporte.builder()
            .rutPersonalS(dto.getRut())
            .nombre(dto.getNombre())
            .apellido(dto.getApellido())
            .email(dto.getEmail())
            .rol(dto.getRol())
            .estado(dto.getEstado())
            .build();

        return personalSoporteRepository.save(personal);
    }

    public PersonalSoporte actualizarPersonal(Long idPersonal, PersonalSoporteDTO dto) {
        PersonalSoporte personal = obtenerPorIdPersonal(idPersonal);
        personal.setNombre(dto.getNombre());
        personal.setApellido(dto.getApellido());
        personal.setEmail(dto.getEmail());
        personal.setRol(dto.getRol());
        personal.setEstado(dto.getEstado());
        return personalSoporteRepository.save(personal);
    }

    public void eliminarPersonal(Long idPersonal) {
        if (!personalSoporteRepository.existsById(idPersonal)) {
            throw new ResourceNotFoundException("Personal de soporte", idPersonal);
        }
        personalSoporteRepository.deleteById(idPersonal);
    }

}
