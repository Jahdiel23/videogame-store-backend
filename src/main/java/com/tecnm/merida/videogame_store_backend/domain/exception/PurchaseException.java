package com.tecnm.merida.videogame_store_backend.domain.exception;

public class PurchaseException extends RuntimeException {
    private final int status;
    public PurchaseException(int status, String message) { super(message); this.status = status; }
    public int getStatus() { return status; }
}
