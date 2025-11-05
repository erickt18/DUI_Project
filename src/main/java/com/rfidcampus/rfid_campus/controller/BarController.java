package com.rfidcampus.rfid_campus.controller;

import com.rfidcampus.rfid_campus.dto.BarRequest;
import com.rfidcampus.rfid_campus.dto.BarResponse;
import com.rfidcampus.rfid_campus.model.Estudiante;
import com.rfidcampus.rfid_campus.service.TarjetaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bar")
public class BarController {

    private final TarjetaService tarjetaService;

    public BarController(TarjetaService tarjetaService) {
        this.tarjetaService = tarjetaService;
    }

    // Escanear tarjeta (validar y obtener info)
    @PostMapping("/scan")
    public ResponseEntity<BarResponse> scanCard(@RequestBody BarRequest data) {
        try {
            Double saldo = tarjetaService.consultarSaldo(data.getUidTarjeta());
            return ResponseEntity.ok(new BarResponse("Tarjeta válida", "Estudiante", saldo));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(new BarResponse(e.getMessage(), null, 0.0));
        }
    }

    // Realizar pago
    @PostMapping("/pagar")
    public ResponseEntity<BarResponse> pagar(@RequestBody BarRequest data) {
        try {
            Estudiante est = tarjetaService.pagar(data.getUidTarjeta(), data.getMonto());
            return ResponseEntity.ok(
                    new BarResponse("Compra realizada", est.getNombreCompleto(), est.getSaldo())
            );
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(new BarResponse(e.getMessage(), null, 0.0));
        }
    }

    // Recargar saldo
    @PostMapping("/recargar")
    public ResponseEntity<BarResponse> recargar(@RequestBody BarRequest data) {
        try {
            Estudiante est = tarjetaService.recargarSaldo(data.getUidTarjeta(), data.getMonto());
            return ResponseEntity.ok(
                    new BarResponse("Saldo recargado", est.getNombreCompleto(), est.getSaldo())
            );
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(new BarResponse(e.getMessage(), null, 0.0));
        }
    }
}