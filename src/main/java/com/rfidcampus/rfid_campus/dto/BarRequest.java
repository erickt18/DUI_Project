package com.rfidcampus.rfid_campus.dto;

import lombok.Data;

@Data
public class BarRequest {
    private String uidTarjeta; // RFID UID
    private Double monto;      // Para pagar o recargar
}
