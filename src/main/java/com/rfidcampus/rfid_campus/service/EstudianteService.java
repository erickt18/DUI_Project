package com.rfidcampus.rfid_campus.service;

import com.rfidcampus.rfid_campus.model.Estudiante;
import com.rfidcampus.rfid_campus.repository.EstudianteRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EstudianteService {

    private final EstudianteRepository estudianteRepository;
    private final PasswordEncoder passwordEncoder;

    public EstudianteService(EstudianteRepository estudianteRepository,
                             PasswordEncoder passwordEncoder) {
        this.estudianteRepository = estudianteRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Estudiante guardar(Estudiante estudiante) {
        // Si viene en texto plano dentro de passwordHash, lo ciframos.
        // (Si tu entidad tiene un campo "password" diferente, ajusta aquí).
        String plano = estudiante.getPasswordHash();
        if (plano != null && !plano.startsWith("$2a$")) { // muy simple: evita re-encifrar si ya es BCrypt
            estudiante.setPasswordHash(passwordEncoder.encode(plano));
        }
        return estudianteRepository.save(estudiante);
    }

    public List<Estudiante> listarTodos() {
        return estudianteRepository.findAll();
    }

    public Optional<Estudiante> buscarPorEmail(String email) {
        return estudianteRepository.findByEmail(email);
    }
}
