package com.SoporteMicroServicio.SoporteM.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.SoporteMicroServicio.SoporteM.model.MensajeSoporte;

@Repository

public interface  MensajeSoporteRepository extends JpaRepository<MensajeSoporte, Long> {

    List<MensajeSoporte> findByTicketIdTicket(Long idTicket);
    
}
