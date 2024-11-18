package com.concesionario.ventas.repository;

import com.concesionario.ventas.entities.Ventas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface VentasRepository extends JpaRepository<Ventas, Long> {
    List<Ventas> findByCustomerId(Long customerId);

    List<Ventas> findByFechaBetween(LocalDate fechaInicial, LocalDate fechaFinal);
}
