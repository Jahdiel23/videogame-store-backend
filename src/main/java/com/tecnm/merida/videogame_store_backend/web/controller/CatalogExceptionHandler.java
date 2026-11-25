package com.tecnm.merida.videogame_store_backend.web.controller;

import com.tecnm.merida.videogame_store_backend.domain.exception.CatalogException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice(assignableTypes = {ConsoleController.class, VideogameController.class})
public class CatalogExceptionHandler {
    public record ApiError(int status, String message) {}
    @ExceptionHandler(CatalogException.class)
    public ResponseEntity<ApiError> catalog(CatalogException ex) {
        return ResponseEntity.status(ex.getStatus()).body(new ApiError(ex.getStatus(), ex.getMessage()));
    }
    @ExceptionHandler({HttpMessageNotReadableException.class, MethodArgumentTypeMismatchException.class})
    public ResponseEntity<ApiError> invalidInput(Exception ex) {
        return ResponseEntity.badRequest().body(new ApiError(400, "JSON o identificador invalido. Revisa los tipos y campos enviados."));
    }
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiError> integrity(DataIntegrityViolationException ex) {
        return ResponseEntity.status(409).body(new ApiError(409, "No se puede completar la operacion: existen datos relacionados o un conflicto de integridad."));
    }
}
