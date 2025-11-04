package com.rfidcampus.rfid_campus.controller;

import com.rfidcampus.rfid_campus.model.Transaccion;
import com.rfidcampus.rfid_campus.repository.TransaccionRepository;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/transacciones")
public class TransaccionController {

    private final TransaccionRepository transaccionRepo;

    public TransaccionController(TransaccionRepository transaccionRepo) {
        this.transaccionRepo = transaccionRepo;
    }

    @GetMapping("/estudiante/{id}")
    public List<Transaccion> historial(@PathVariable Long id) {
        return transaccionRepo.findByEstudianteId(id);
    }
}
