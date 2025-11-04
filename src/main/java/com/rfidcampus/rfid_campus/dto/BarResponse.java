package com.rfidcampus.rfid_campus.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BarResponse {
    private String mensaje;
    private String nombre;
    private Double saldo;
}
