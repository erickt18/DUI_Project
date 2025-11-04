package com.rfidcampus.rfid_campus.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tarjetas_rfid")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class TarjetaRfid {

    @Id
    @Column(name = "tarjeta_uid", length = 32)
    private String tarjetaUid; // Usamos el UID como PK (tu tabla no tiene id autoincremental)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_estudiante_fk", nullable = false)
    private Estudiante estudiante;

    @Column(name = "estado", length = 20)
    private String estado; // ACTIVA / INACTIVA

    // Si decides que el saldo “vive” en Estudiante, puedes omitir este campo:
    // @Column(name = "saldo")
    // private Double saldo;
}
