package com.rfidcampus.rfid_campus.controller;

import com.rfidcampus.rfid_campus.model.Estudiante;
import com.rfidcampus.rfid_campus.service.EstudianteService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/estudiantes")
public class EstudianteController {

    private final EstudianteService estudianteService;

    public EstudianteController(EstudianteService estudianteService) {
        this.estudianteService = estudianteService;
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
    public Estudiante buscar(@RequestParam String email) {
        return estudianteService.buscarPorEmail(email).orElse(null);
    }

}
