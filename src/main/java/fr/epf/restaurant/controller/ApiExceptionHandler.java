package fr.epf.restaurant.controller;

import fr.epf.restaurant.exception.BadRequestException;
import fr.epf.restaurant.exception.ErreurResponse;
import fr.epf.restaurant.exception.InvalidTransitionException;
import fr.epf.restaurant.exception.NotFoundException;
import fr.epf.restaurant.exception.StockInsuffisantException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErreurResponse> handleNotFound(NotFoundException ex) {
        return build(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler({BadRequestException.class, InvalidTransitionException.class})
    public ResponseEntity<ErreurResponse> handleBadRequest(RuntimeException ex) {
        return build(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(StockInsuffisantException.class)
    public ResponseEntity<ErreurResponse> handleStock(StockInsuffisantException ex) {
        return build(HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErreurResponse> handleGeneric(Exception ex) {
        return build(HttpStatus.INTERNAL_SERVER_ERROR, "Erreur interne serveur");
    }

    private ResponseEntity<ErreurResponse> build(HttpStatus status, String message) {
        return ResponseEntity.status(status).body(new ErreurResponse(status.value(), message));
    }
}
