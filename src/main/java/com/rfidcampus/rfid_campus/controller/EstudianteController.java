package com.rfidcampus.rfid_campus.controller;

import com.rfidcampus.rfid_campus.model.Estudiante;
import com.rfidcampus.rfid_campus.model.Transaccion;
import com.rfidcampus.rfid_campus.repository.TransaccionRepository;
import com.rfidcampus.rfid_campus.service.EstudianteService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/estudiantes")
public class EstudianteController {

    private final EstudianteService estudianteService;
    private final TransaccionRepository transaccionRepo;

    public EstudianteController(EstudianteService estudianteService, 
                                TransaccionRepository transaccionRepo) {
        this.estudianteService = estudianteService;
        this.transaccionRepo = transaccionRepo;
    }

    @PostMapping("/registrar")
    public ResponseEntity<Estudiante> registrar(@RequestBody Estudiante estudiante) {
        Estudiante nuevo = estudianteService.guardar(estudiante);
        return ResponseEntity.ok(nuevo);
    }

    @GetMapping("/listar")
    public List<Estudiante> listar() {
        return estudianteService.listarTodos();
    }

    @GetMapping("/buscar")
    public ResponseEntity<Estudiante> buscar(@RequestParam String email) {
        return estudianteService.buscarPorEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ✅ NUEVO: Obtener datos del estudiante por ID
    @GetMapping("/{id}")
    public ResponseEntity<Estudiante> obtenerPorId(@PathVariable Long id) {
        return estudianteService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ✅ NUEVO: Historial de transacciones
    @GetMapping("/{id}/transacciones")
    public ResponseEntity<List<Transaccion>> obtenerHistorial(@PathVariable Long id) {
        List<Transaccion> historial = transaccionRepo.findByEstudianteIdOrderByFechaDesc(id);
        return ResponseEntity.ok(historial);
    }

    // ✅ NUEVO: Consultar saldo por ID
    @GetMapping("/{id}/saldo")
    public ResponseEntity<Double> consultarSaldo(@PathVariable Long id) {
        return estudianteService.buscarPorId(id)
                .map(est -> ResponseEntity.ok(est.getSaldo()))
                .orElse(ResponseEntity.notFound().build());
    }
}