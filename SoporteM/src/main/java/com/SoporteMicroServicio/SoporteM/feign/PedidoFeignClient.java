package com.SoporteMicroServicio.SoporteM.feign;

import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.SoporteMicroServicio.SoporteM.feign.fallback.PedidoFeignFallback;

@FeignClient(name = "pedido-service", url = "${pedido.service.url}", fallback = PedidoFeignFallback.class)
public interface PedidoFeignClient {

    @GetMapping("/api/pedidos/{id}")
    Map<String, Object> obtenerPedidoPorId(@PathVariable("id") Long idPedido);

}
