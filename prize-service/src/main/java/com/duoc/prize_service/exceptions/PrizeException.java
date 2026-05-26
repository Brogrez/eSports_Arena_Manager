package com.duoc.prize_service.exceptions; // Alineado a tu ruta física real actual

public class PrizeException extends RuntimeException {

    public PrizeException(String message) {
        super(message);
    }
}