package com.rfidcampus.rfid_campus.service;

import com.rfidcampus.rfid_campus.model.Estudiante;
import com.rfidcampus.rfid_campus.model.TarjetaRfid;
import com.rfidcampus.rfid_campus.model.Transaccion;
import com.rfidcampus.rfid_campus.repository.EstudianteRepository;
import com.rfidcampus.rfid_campus.repository.TarjetaRfidRepository;
import com.rfidcampus.rfid_campus.repository.TransaccionRepository;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TarjetaService {

    private final TarjetaRfidRepository tarjetaRepo;
    private final EstudianteRepository estudianteRepo;
    private final TransaccionRepository transaccionRepo;

    public TarjetaService(
            TarjetaRfidRepository tarjetaRepo,
            EstudianteRepository estudianteRepo,
            TransaccionRepository transaccionRepo
    ) {
        this.tarjetaRepo = tarjetaRepo;
        this.estudianteRepo = estudianteRepo;
        this.transaccionRepo = transaccionRepo;
    }

    public TarjetaRfid guardar(TarjetaRfid tarjeta) {
        return tarjetaRepo.save(tarjeta);
    }

    public List<TarjetaRfid> listar() {
        return tarjetaRepo.findAll();
    }

    public TarjetaRfid asignarTarjeta(String tarjetaUid, Long idEstudiante) {
        TarjetaRfid tarjeta = tarjetaRepo.findById(tarjetaUid)
                .orElseThrow(() -> new RuntimeException("Tarjeta no encontrada"));

        Estudiante estudiante = estudianteRepo.findById(idEstudiante)
                .orElseThrow(() -> new RuntimeException("Estudiante no encontrado"));

        tarjeta.setEstudiante(estudiante);
        return tarjetaRepo.save(tarjeta);
    }

    // ✅ RECARGAR SALDO + REGISTRO AUTOMÁTICO
    public TarjetaRfid recargarSaldo(String uid, Double monto) {
        TarjetaRfid tarjeta = tarjetaRepo.findById(uid)
                .orElseThrow(() -> new RuntimeException("Tarjeta no encontrada"));

        tarjeta.setSaldo(tarjeta.getSaldo() + monto);
        tarjetaRepo.save(tarjeta);

        transaccionRepo.save(
                Transaccion.builder()
                        .estudiante(tarjeta.getEstudiante())
                        .tipo("RECARGA")
                        .monto(monto)
                        .fecha(LocalDateTime.now())
                        .build()
        );

        return tarjeta;
    }

    // ✅ CONSULTAR SALDO
    public Double consultarSaldo(String uid) {
        TarjetaRfid tarjeta = tarjetaRepo.findById(uid)
                .orElseThrow(() -> new RuntimeException("Tarjeta no encontrada"));

        return tarjeta.getSaldo();
    }

    // ✅ PAGAR / DESCONTAR SALDO + REGISTRO AUTOMÁTICO
    public TarjetaRfid pagar(String uid, Double monto) {
        TarjetaRfid tarjeta = tarjetaRepo.findById(uid)
                .orElseThrow(() -> new RuntimeException("Tarjeta no encontrada"));

        if (tarjeta.getSaldo() < monto) {
            throw new RuntimeException("Saldo insuficiente");
        }

        tarjeta.setSaldo(tarjeta.getSaldo() - monto);
        tarjetaRepo.save(tarjeta);

        transaccionRepo.save(
                Transaccion.builder()
                        .estudiante(tarjeta.getEstudiante())
                        .tipo("COMPRA_BAR")
                        .monto(monto)
                        .fecha(LocalDateTime.now())
                        .build()
        );

        return tarjeta;
    }
}
