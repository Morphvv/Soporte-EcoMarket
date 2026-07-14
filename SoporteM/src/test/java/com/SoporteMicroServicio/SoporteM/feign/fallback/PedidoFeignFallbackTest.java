package com.SoporteMicroServicio.SoporteM.feign.fallback;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.util.Map;

class PedidoFeignFallbackTest {

    private final PedidoFeignFallback fallback = new PedidoFeignFallback();

    @Test
    void obtenerPedidoPorId_retornaDatosDefault() {
        Map<String, Object> resultado = fallback.obtenerPedidoPorId(10L);

        assertNotNull(resultado);
        assertEquals(10L, resultado.get("id"));
        assertEquals("DESCONOCIDO", resultado.get("estado"));
    }
}
