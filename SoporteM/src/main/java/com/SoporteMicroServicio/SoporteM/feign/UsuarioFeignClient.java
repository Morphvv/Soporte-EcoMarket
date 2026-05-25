package com.SoporteMicroServicio.SoporteM.feign;

import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.SoporteMicroServicio.SoporteM.feign.fallback.UsuarioFeignFallback;

@FeignClient(name = "usuario-service", url = "${usuario.service.url}", fallback = UsuarioFeignFallback.class)
public interface UsuarioFeignClient {

    @GetMapping("/api/v1/usuarios/buscar/{idUsuario}")
    Map<String, Object> obtenerUsuarioPorRut(@PathVariable("idUsuario") Long rut);

}
