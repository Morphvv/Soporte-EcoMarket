package com.SoporteMicroServicio.SoporteM.feign.fallback;

import com.SoporteMicroServicio.SoporteM.feign.UsuarioFeignClient;

public  class UsuarioFeignFallback implements UsuarioFeignClient {

    @Override
    public Map<String, Object> obtenerUsuarioPorRut(Long rut) {
        log.error("Microservicio de usuario no disponible")
        return Map.of("rut",           rut,
            "nombre",        "Usuario no disponible",
            "apellido",      "N/A",
            "email",         "N/A",
            "estadoUsuario", "DESCONOCIDO"
        );
    }
    
    }
    
}
