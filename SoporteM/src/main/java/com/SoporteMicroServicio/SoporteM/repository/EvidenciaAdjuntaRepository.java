package com.SoporteMicroServicio.SoporteM.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.SoporteMicroServicio.SoporteM.model.EvidenciaAdjunta;

@Repository

public interface  EvidenciaAdjuntaRepository extends JpaRepository<EvidenciaAdjunta, Long>{

    List<EvidenciaAdjunta> findByTicketIdTicket(Long idTicket);
    
}
