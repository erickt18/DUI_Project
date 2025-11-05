package com.rfidcampus.rfid_campus.repository;

import com.rfidcampus.rfid_campus.model.Transaccion;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TransaccionRepository extends JpaRepository<Transaccion, Long> {
    // Historial ordenado por fecha descendente
    List<Transaccion> findByEstudianteIdOrderByFechaDesc(Long estudianteId);
}