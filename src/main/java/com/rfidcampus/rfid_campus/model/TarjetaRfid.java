package com.rfidcampus.rfid_campus.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tarjetas_rfid")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class TarjetaRfid {

    @Id
    @Column(name = "tarjeta_uid", length = 32)
    private String tarjetaUid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_estudiante_fk", nullable = true) // ✅ CAMBIAR A TRUE
    private Estudiante estudiante;

    @Column(name = "estado", length = 20)
    private String estado;
}