package com.SoporteMicroServicio.SoporteM.service;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.SoporteMicroServicio.SoporteM.dto.PersonalSoporteDTO;
import com.SoporteMicroServicio.SoporteM.exception.BusinessException;
import com.SoporteMicroServicio.SoporteM.exception.ResourceNotFoundException;
import com.SoporteMicroServicio.SoporteM.model.PersonalSoporte;
import com.SoporteMicroServicio.SoporteM.repository.PersonalSoporteRepository;

@ExtendWith(MockitoExtension.class)
class PersonalSoporteServiceTest {

    @Mock
    private PersonalSoporteRepository personalSoporteRepository;

    @InjectMocks
    private PersonalSoporteService personalSoporteService;

    @Test
    void listarTodos(){
        PersonalSoporte p1 = PersonalSoporte.builder().rutPersonalS(11111111L).nombre("Ana").rol("CAJERA").build();
        PersonalSoporte p2 = PersonalSoporte.builder().rutPersonalS(22222222L).nombre("Luis").rol("SUPERVISOR").build();
        when(personalSoporteRepository.findAll()).thenReturn(List.of(p1, p2));

        List<PersonalSoporte> resultado = personalSoporteService.listarTodos();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(personalSoporteRepository, times(1)).findAll();
    }

    @Test
    void obtenerPorIdPersonal(){
        PersonalSoporte personal = PersonalSoporte.builder()
                .rutPersonalS(12345678L).nombre("Carlos").email("carlos@eco.com").build();
        when(personalSoporteRepository.findById(12345678L)).thenReturn(Optional.of(personal));

        PersonalSoporte resultado = personalSoporteService.obtenerPorIdPersonal(12345678L);

        assertNotNull(resultado);
        assertEquals(12345678L, resultado.getRutPersonalS());
    }

    @Test
    void obtenerPorIdPersonal_noExiste(){
        when(personalSoporteRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> personalSoporteService.obtenerPorIdPersonal(99L));
    }

    @Test
    void crearPersonal(){
        PersonalSoporteDTO dto = new PersonalSoporteDTO();
        dto.setRut(12345678L);
        dto.setNombre("Maria");
        dto.setApellido("Lopez");
        dto.setEmail("maria@eco.com");
        dto.setRol("AGENTE");
        dto.setEstado("ACTIVO");

        PersonalSoporte guaradado = PersonalSoporte.builder()
                .rutPersonalS(12345678L).nombre("Maria").email("maria@eco.com").rol("REPONEDORA").build();

        when(personalSoporteRepository.findByEmail("maria@eco.com")).thenReturn(Optional.empty());
        when(personalSoporteRepository.save(any(PersonalSoporte.class))).thenReturn(guaradado);

        PersonalSoporte resultado = personalSoporteService.crearPersonal(dto);

        assertNotNull(resultado);
        assertEquals("REPONEDORA", resultado.getRol());
        verify(personalSoporteRepository, times(1)).save(any(PersonalSoporte.class));
    }

    @Test
    void creraPersonal_emailDuplicado(){
        PersonalSoporteDTO dto = new PersonalSoporteDTO();
        dto.setEmail("duplicado@eco.com");

        PersonalSoporte existente = PersonalSoporte.builder().email("duplicado@eco.com").build();
        when(personalSoporteRepository.findByEmail("duplicado@eco.com")).thenReturn(Optional.of(existente));

        assertThrows(BusinessException.class, () -> personalSoporteService.crearPersonal(dto));
    }

    @Test
    void actualizarPersonal(){
        PersonalSoporte personal = PersonalSoporte.builder()
            .rutPersonalS(12345678L).nombre("Viejo").email("old@eco.com").rol("AUXILIAR").build();

        PersonalSoporteDTO dto = new PersonalSoporteDTO();
        dto.setNombre("Nuevo");
        dto.setApellido("Apellido");
        dto.setEmail("new@eco.com");
        dto.setRol("SUPERVISOR");
        dto.setEstado("ACTIVO");

        when(personalSoporteRepository.findById(12345678L)).thenReturn(Optional.of(personal));
        when(personalSoporteRepository.save(any(PersonalSoporte.class))).thenReturn(personal);

        PersonalSoporte resultado = personalSoporteService.actualizarPersonal(12345678L, dto);

        assertNotNull(resultado);
        verify(personalSoporteRepository, times(1)).save(any(PersonalSoporte.class));
    }

    @Test
    void eliminarPersonal(){
        when(personalSoporteRepository.existsById(12345678L)).thenReturn(true);

        personalSoporteService.eliminarPersonal(12345678L);

        verify(personalSoporteRepository, times(1)).deleteById(12345678L);
    }

    @Test
    void eliminarPersonal_noExiste(){
        when(personalSoporteRepository.existsById(99L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> personalSoporteService.eliminarPersonal(99L));
    }
}
