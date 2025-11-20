package com.tiendafunciona.repository;

import com.tiendafunciona.domain.Ruta;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RutaRepository extends JpaRepository<Ruta, Integer> {
    List<Ruta> findAllByOrderByRequiereRolAsc();
}