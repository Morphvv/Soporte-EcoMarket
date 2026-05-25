package com.SoporteMicroServicio.SoporteM.feign;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "usuario-service", url = "${usuario.service.url}", fallback = UsuarioFeignFallback.class)

public interface UsuarioFeignClient {

    @GetMapping("/api/v1/usuarios/buscar/{idUsuario}")
    Map<String, Object> obtenerUsuarioPorRut(@PathVariable("rut")Long rut);
    
}
