package com.rfidcampus.rfid_campus.controller;

import com.rfidcampus.rfid_campus.dto.BarRequest;
import com.rfidcampus.rfid_campus.dto.BarResponse;
import com.rfidcampus.rfid_campus.model.Estudiante;
import com.rfidcampus.rfid_campus.model.TarjetaRfid;
import com.rfidcampus.rfid_campus.repository.EstudianteRepository;
import com.rfidcampus.rfid_campus.repository.TarjetaRfidRepository;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bar")
public class BarController {

    private final TarjetaRfidRepository tarjetaRepo;
    private final EstudianteRepository estudianteRepo;

    public BarController(TarjetaRfidRepository tarjetaRepo, EstudianteRepository estudianteRepo) {
        this.tarjetaRepo = tarjetaRepo;
        this.estudianteRepo = estudianteRepo;
    }

    @PostMapping("/scan")
    public BarResponse scanCard(@RequestBody BarRequest data) {
        TarjetaRfid tarjeta = tarjetaRepo
                .findByTarjetaUidAndEstado(data.getUidTarjeta(), "ACTIVA")
                .orElseThrow(() -> new RuntimeException("Tarjeta no registrada"));

        Estudiante est = tarjeta.getEstudiante();
        return new BarResponse("Tarjeta válida", est.getNombreCompleto(), est.getSaldo());
    }

    @PostMapping("/pagar")
    public BarResponse pagar(@RequestBody BarRequest data) {
        TarjetaRfid tarjeta = tarjetaRepo
                .findByTarjetaUidAndEstado(data.getUidTarjeta(), "ACTIVA")
                .orElseThrow(() -> new RuntimeException("Tarjeta no registrada"));

        Estudiante est = tarjeta.getEstudiante();

        if (est.getSaldo() < data.getMonto()) {
            return new BarResponse("Saldo insuficiente", est.getNombreCompleto(), est.getSaldo());
        }
        est.setSaldo(est.getSaldo() - data.getMonto());
        estudianteRepo.save(est);

        return new BarResponse("Compra realizada", est.getNombreCompleto(), est.getSaldo());
    }

    @PostMapping("/recargar")
    public BarResponse recargar(@RequestBody BarRequest data) {
        TarjetaRfid tarjeta = tarjetaRepo
                .findByTarjetaUidAndEstado(data.getUidTarjeta(), "ACTIVA")
                .orElseThrow(() -> new RuntimeException("Tarjeta no registrada"));

        Estudiante est = tarjeta.getEstudiante();
        est.setSaldo(est.getSaldo() + data.getMonto());
        estudianteRepo.save(est);

        return new BarResponse("Saldo recargado", est.getNombreCompleto(), est.getSaldo());
    }
}
