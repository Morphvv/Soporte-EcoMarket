package com.SoporteMicroServicio.SoporteM.feign.fallback;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.SoporteMicroServicio.SoporteM.feign.UsuarioFeignClient;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class UsuarioFeignFallback implements UsuarioFeignClient {

    @Override
    public Map<String, Object> obtenerUsuarioPorRut(Long rut) {
        log.error("Microservicio de usuario no disponible");
        return Map.of(
            "rut",           rut,
            "nombre",        "Usuario no disponible",
            "apellido",      "N/A",
            "email",         "N/A",
            "estadoUsuario", "DESCONOCIDO"
        );
    }

}
