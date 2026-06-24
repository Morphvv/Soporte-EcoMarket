package com.SoporteMicroServicio.SoporteM.feign.fallback;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.util.Map;

class UsuarioFeignFallbackTest {

    private final UsuarioFeignFallback fallback = new UsuarioFeignFallback();

    @Test
    void obtenerUsuarioPorRut_retornaDatosDefault() {
        Map<String, Object> resultado = fallback.obtenerUsuarioPorRut(12345678L);

        assertNotNull(resultado);
        assertEquals("Usuario no disponible", resultado.get("nombre"));
        assertEquals(12345678L, resultado.get("rut"));
        assertEquals("DESCONOCIDO", resultado.get("estadoUsuario"));
    }
}
