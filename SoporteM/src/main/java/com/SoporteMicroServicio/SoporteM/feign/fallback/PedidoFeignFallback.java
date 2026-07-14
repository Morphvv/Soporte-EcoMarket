package com.SoporteMicroServicio.SoporteM.feign.fallback;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.SoporteMicroServicio.SoporteM.feign.PedidoFeignClient;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class PedidoFeignFallback implements PedidoFeignClient {

    @Override
    public Map<String, Object> obtenerPedidoPorId(Long idPedido) {
        log.error("Microservicio de pedido no disponible");
        return Map.of(
            "id",     idPedido,
            "estado", "DESCONOCIDO"
        );
    }

}
