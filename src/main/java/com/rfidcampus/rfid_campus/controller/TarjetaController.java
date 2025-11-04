package com.rfidcampus.rfid_campus.controller;

import com.rfidcampus.rfid_campus.dto.AsignarTarjetaDTO;
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

    //  Registrar tarjeta
    @PostMapping("/registrar")
    public ResponseEntity<TarjetaRfid> registrar(@RequestBody TarjetaRfid tarjeta) {
        return ResponseEntity.ok(tarjetaService.guardar(tarjeta));
    }

    //  Asignar tarjeta usando DTO (forma principal)
    @PutMapping("/asignar")
    public ResponseEntity<TarjetaRfid> asignarTarjeta(@RequestBody AsignarTarjetaDTO dto) {
        return ResponseEntity.ok(
                tarjetaService.asignarTarjeta(dto.getTarjetaUid(), dto.getIdEstudiante()));
    }

    //  (Opcional) Asignar por URL si deseas probar rápido
    @PutMapping("/asignar/{uid}/{idEstudiante}")
    public ResponseEntity<TarjetaRfid> asignar(
            @PathVariable String uid,
            @PathVariable Long idEstudiante) {
        return ResponseEntity.ok(tarjetaService.asignarTarjeta(uid, idEstudiante));
    }

    //  Consultar saldo
    @GetMapping("/saldo/{uid}")
    public ResponseEntity<Double> consultarSaldo(@PathVariable String uid) {
        return ResponseEntity.ok(tarjetaService.consultarSaldo(uid));
    }

    //  Listar tarjetas
    @GetMapping("/listar")
    public List<TarjetaRfid> listar() {
        return tarjetaService.listar();
    }

    //  Recargar saldo
    @PutMapping("/recargar")
    public ResponseEntity<TarjetaRfid> recargar(@RequestBody Map<String, Object> body) {
        String uid = (String) body.get("tarjetaUid");
        Double monto = Double.valueOf(body.get("monto").toString());
        return ResponseEntity.ok(tarjetaService.recargarSaldo(uid, monto));
    }

    //  Pago en bar / biblioteca / cafetín
    @PutMapping("/pagar")
    public ResponseEntity<TarjetaRfid> pagar(@RequestBody Map<String, Object> body) {
        String uid = (String) body.get("tarjetaUid");
        Double monto = Double.valueOf(body.get("monto").toString());
        return ResponseEntity.ok(tarjetaService.pagar(uid, monto));
    }
}
