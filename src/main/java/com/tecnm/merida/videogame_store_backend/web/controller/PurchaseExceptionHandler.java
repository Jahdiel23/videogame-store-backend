package com.tecnm.merida.videogame_store_backend.web.controller;

import com.tecnm.merida.videogame_store_backend.domain.exception.PurchaseException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.PessimisticLockingFailureException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
@RestControllerAdvice(assignableTypes = PurchaseController.class)
public class PurchaseExceptionHandler {
    public record ApiError(int status, String message) {}
    @ExceptionHandler(PurchaseException.class)
    public ResponseEntity<ApiError> purchase(PurchaseException ex) {
        return ResponseEntity.status(ex.getStatus()).body(new ApiError(ex.getStatus(), ex.getMessage()));
    }
    @ExceptionHandler({HttpMessageNotReadableException.class, MethodArgumentTypeMismatchException.class})
    public ResponseEntity<ApiError> invalid(Exception ex) {
        return ResponseEntity.badRequest().body(new ApiError(400, "JSON o identificador invalido. Revisa los campos y sus tipos."));
    }
    @ExceptionHandler({DataIntegrityViolationException.class, PessimisticLockingFailureException.class})
    public ResponseEntity<ApiError> conflict(Exception ex) {
        return ResponseEntity.status(409).body(new ApiError(409, "No se pudo completar la compra por un conflicto de datos. Revisa los registros e intenta nuevamente."));
    }
}
