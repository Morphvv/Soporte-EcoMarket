package com.SoporteMicroServicio.SoporteM.service;

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

    public List<PersonalSoporte> listarTodos(){
        return personalSoporteRepository.findAll();
    }

    public PersonalSoporte obtenerPorIdPersonal(Long idPersonal){
        return personalSoporteRepository.findByIdPersonal(idPersonal).orElseThrow(() -> new ResourceNotFoundException("Personal de soporte no encontrado", idPersonal));
    }

    public PersonalSoporte crearPersonal(PersonalSoporteDTO dto){
        log.info("Creando personal de soporte: {}", dto.getRut());

        if (personalSoporteRepository.findByRut(dto.getRut().isPresent())){
            throw new BusinessException("Ya existe personal registrado con ese rut");
        }

        PersonalSoporte personal = PersonalSoporte.builder()
                .nombre(dto.getNombre())
                .apellido(dto.getApellido())
                .email(dto.getEmail())
                .rol(dto.getRol())
                .estado(dto.getEstado())
                .build();

        return personalRepository.save(personal);
    }

    public PersonalSoporte actualizarPersonal(Long idPersonal, PersonalSoporteDTO dto){
        PersonalSoporte personal = obtenerPorIdPersonal(idPersonal);
        p.setNombre(dto.getNombre());
        p.setApellido(dto.getApellido());
        p.setEmail(dto.getEmail());
        p.setRol(dto.getRol());
        p.setEstado(dto.getEstado());
        return personalRepository.save(p);

    }

    public void eliminarPersonal(Long idPersonal){
        if (!personalSoporteRepository.existsByIdPersonal(idPersonal)){
            throw new ResourceNotFoundException("Personal de soporte eliminado", idPersonal);
            personalSoporteRepository.deleteByIdPersonal(idPersonal);
        }
    }
    }
}
