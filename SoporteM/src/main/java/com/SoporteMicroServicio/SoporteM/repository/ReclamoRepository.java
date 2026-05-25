package com.SoporteMicroServicio.SoporteM.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.SoporteMicroServicio.SoporteM.model.Reclamo;

@Repository
public interface ReclamoRepository extends JpaRepository<Reclamo, Long> {

    Optional<Reclamo> findByTicketSoporteIdTicket(Long idTicket);
    List<Reclamo> findByEstadoReclamo(String estadoReclamo);

}
