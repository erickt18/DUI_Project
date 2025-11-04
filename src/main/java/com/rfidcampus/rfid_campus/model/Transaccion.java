package com.rfidcampus.rfid_campus.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "transacciones")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_estudiante_fk", nullable = false)
    private Estudiante estudiante;

    @Column(nullable = false)
    private String tipo; // RECARGA | COMPRA_BAR | COMPRA_BIBLIOTECA

    @Column(nullable = false)
    private Double monto;

    @Column(nullable = false)
    private LocalDateTime fecha = LocalDateTime.now();
}
