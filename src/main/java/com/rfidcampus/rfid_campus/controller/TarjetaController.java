package com.rfidcampus.rfid_campus.controller;

import com.rfidcampus.rfid_campus.dto.AsignarTarjetaDTO;
import com.rfidcampus.rfid_campus.model.Estudiante;
import com.rfidcampus.rfid_campus.model.TarjetaRfid;
import com.rfidcampus.rfid_campus.service.TarjetaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tarjetas")
public class TarjetaController {

    private final TarjetaService tarjetaService;

    public TarjetaController(TarjetaService tarjetaService) {
        this.tarjetaService = tarjetaService;
    }

    // ✅ CORREGIDO: Registrar tarjeta sin estudiante
    @PostMapping("/registrar")
    public ResponseEntity<?> registrar(@RequestBody Map<String, String> body) {
        try {
            TarjetaRfid tarjeta = TarjetaRfid.builder()
                    .tarjetaUid(body.get("tarjetaUid"))
                    .estado(body.get("estado"))
                    .estudiante(null) // Sin asignar por ahora
                    .build();
            
            TarjetaRfid saved = tarjetaService.guardar(tarjeta);
            return ResponseEntity.ok(Map.of(
                "mensaje", "Tarjeta registrada exitosamente",
                "tarjetaUid", saved.getTarjetaUid(),
                "estado", saved.getEstado()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // ✅ Asignar tarjeta usando DTO
    @PutMapping("/asignar")
    public ResponseEntity<?> asignarTarjeta(@RequestBody AsignarTarjetaDTO dto) {
        try {
            TarjetaRfid tarjeta = tarjetaService.asignarTarjeta(dto.getTarjetaUid(), dto.getIdEstudiante());
            return ResponseEntity.ok(Map.of(
                "mensaje", "Tarjeta asignada exitosamente",
                "tarjetaUid", tarjeta.getTarjetaUid(),
                "estudiante", tarjeta.getEstudiante().getNombreCompleto()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // ✅ Consultar saldo
    @GetMapping("/saldo/{uid}")
    public ResponseEntity<?> consultarSaldo(@PathVariable String uid) {
        try {
            Double saldo = tarjetaService.consultarSaldo(uid);
            return ResponseEntity.ok(Map.of("saldo", saldo));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // ✅ Listar tarjetas
    @GetMapping("/listar")
    public List<TarjetaRfid> listar() {
        return tarjetaService.listar();
    }

    // ✅ Recargar saldo
    @PutMapping("/recargar")
    public ResponseEntity<?> recargar(@RequestBody Map<String, Object> body) {
        try {
            String uid = (String) body.get("tarjetaUid");
            Double monto = Double.valueOf(body.get("monto").toString());
            Estudiante est = tarjetaService.recargarSaldo(uid, monto);
            return ResponseEntity.ok(Map.of(
                "mensaje", "Saldo recargado exitosamente",
                "estudiante", est.getNombreCompleto(),
                "nuevoSaldo", est.getSaldo()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // ✅ Pagar
    @PutMapping("/pagar")
    public ResponseEntity<?> pagar(@RequestBody Map<String, Object> body) {
        try {
            String uid = (String) body.get("tarjetaUid");
            Double monto = Double.valueOf(body.get("monto").toString());
            Estudiante est = tarjetaService.pagar(uid, monto);
            return ResponseEntity.ok(Map.of(
                "mensaje", "Pago realizado exitosamente",
                "estudiante", est.getNombreCompleto(),
                "nuevoSaldo", est.getSaldo()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}