package com.SoporteMicroServicio.SoporteM.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.SoporteMicroServicio.SoporteM.model.ResolucionSoporte;

@Repository
public interface  ResolucionSoporteRepository extends JpaRepository<ResolucionSoporte, Long>{

    Optional<ResolucionSoporte> findByTicketIdTicket(Long idTicket);
}
