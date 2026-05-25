package com.SoporteMicroServicio.SoporteM.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.SoporteMicroServicio.SoporteM.model.HistorialEstadoTicket;

@Repository

public interface HistorialEstadoTicketRepository extends JpaRepository<HistorialEstadoTicket, Long>{
    
    List<HistorialEstadoTicket> findByTicketIdTicketByFechaCambio(Long idTicket);
}
