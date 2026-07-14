package com.SoporteMicroServicio.SoporteM.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.SoporteMicroServicio.SoporteM.dto.ReclamoDTO;
import com.SoporteMicroServicio.SoporteM.exception.BusinessException;
import com.SoporteMicroServicio.SoporteM.exception.ResourceNotFoundException;
import com.SoporteMicroServicio.SoporteM.feign.PedidoFeignClient;
import com.SoporteMicroServicio.SoporteM.model.Reclamo;
import com.SoporteMicroServicio.SoporteM.model.TicketSoporte;
import com.SoporteMicroServicio.SoporteM.repository.ReclamoRepository;
import com.SoporteMicroServicio.SoporteM.repository.TicketSoporteRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor

public class ReclamoService {

    private final ReclamoRepository reclamoRepository;
    private final TicketSoporteRepository ticketSoporteRepository;
    private final PedidoFeignClient pedidoFeignClient;

    public List<Reclamo> listarTodosReclamos() {
        return reclamoRepository.findAll();
    }

    public Reclamo obtenerPorIdReclamo(Long idReclamo) {
        return reclamoRepository.findById(idReclamo)
            .orElseThrow(() -> new ResourceNotFoundException("Reclamo", idReclamo));
    }

    public Reclamo registrarReclamo(Long idTicket, ReclamoDTO dto) {
        log.info("Registrando reclamo para ticket: {}", idTicket);

        TicketSoporte ticket = ticketSoporteRepository.findById(idTicket)
            .orElseThrow(() -> new ResourceNotFoundException("Ticket", idTicket));

        if (reclamoRepository.findByTicketSoporteIdTicket(idTicket).isPresent()) {
            throw new BusinessException("El ticket ya tiene un reclamo registrado");
        }

        Map<String, Object> pedido = pedidoFeignClient.obtenerPedidoPorId(dto.getIdPedido());
        if ("DESCONOCIDO".equals(pedido.get("estado"))) {
            log.error("No se pudo validar el pedido, id: {}", dto.getIdPedido());
            throw new BusinessException("No se pudo registrar el reclamo: pedido no validado");
        }

        Reclamo reclamo = Reclamo.builder()
            .idPedido(dto.getIdPedido())
            .idProducto(dto.getIdProducto())
            .motivo(dto.getMotivo())
            .descripcion(dto.getDescripcion())
            .estadoReclamo("EN_REVISION")
            .fechaReclamo(LocalDateTime.now())
            .ticketSoporte(ticket)
            .build();

        return reclamoRepository.save(reclamo);
    }

    public Reclamo revisarReclamo(Long idReclamo) {
        log.info("Revisando reclamo {}", idReclamo);
        Reclamo r = obtenerPorIdReclamo(idReclamo);
        r.setEstadoReclamo("REVISADO");
        return reclamoRepository.save(r);
    }

    public Reclamo actualizarEstado(Long idReclamo, String nuevoEstado) {
        log.info("Actualizando estado del reclamo {} a {}", idReclamo, nuevoEstado);

        if (!List.of("EN_REVISION", "REVISADO", "RESUELTO", "RECHAZADO").contains(nuevoEstado)) {
            throw new BusinessException("Estado de reclamo invalido: " + nuevoEstado);
        }

        Reclamo r = obtenerPorIdReclamo(idReclamo);
        r.setEstadoReclamo(nuevoEstado);
        return reclamoRepository.save(r);
    }

}
