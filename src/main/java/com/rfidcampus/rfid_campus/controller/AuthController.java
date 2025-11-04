package com.rfidcampus.rfid_campus.controller;

import com.rfidcampus.rfid_campus.dto.AuthResponse;
import com.rfidcampus.rfid_campus.dto.LoginRequest;
import com.rfidcampus.rfid_campus.model.Estudiante;
import com.rfidcampus.rfid_campus.repository.EstudianteRepository;
import com.rfidcampus.rfid_campus.security.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authManager;
    private final JwtService jwtService;
    private final EstudianteRepository estudianteRepository;
    private final PasswordEncoder encoder;

    public AuthController(AuthenticationManager authManager,
                          JwtService jwtService,
                          EstudianteRepository estudianteRepository,
                          PasswordEncoder encoder) {
        this.authManager = authManager;
        this.jwtService = jwtService;
        this.estudianteRepository = estudianteRepository;
        this.encoder = encoder;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest req) {
        try {
            var tokenReq = new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword());
            authManager.authenticate(tokenReq);

            Estudiante est = estudianteRepository.findByEmail(req.getEmail()).orElseThrow();

            String token = jwtService.generateToken(est.getEmail(), Map.of(
                "id", est.getId(),
                "nombre", est.getNombreCompleto(),
                "rol", "STUDENT"
            ));

            return ResponseEntity.ok(new AuthResponse(token));
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(401).body(new AuthResponse("Credenciales incorrectas"));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Estudiante est) {
        if (estudianteRepository.findByEmail(est.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Email ya registrado");
        }

        est.setPasswordHash(encoder.encode(est.getPasswordHash()));
        est.setActivo(true);

        estudianteRepository.save(est);

        return ResponseEntity.ok("Usuario registrado correctamente");
    }
}
