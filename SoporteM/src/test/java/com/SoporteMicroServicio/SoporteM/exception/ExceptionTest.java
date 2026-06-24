package com.SoporteMicroServicio.SoporteM.exception;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.util.Map;

class ExceptionTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void resourceNotFoundException_mensajeDirecto() {
        ResourceNotFoundException ex = new ResourceNotFoundException("El recurso solicitado no existe");
        assertEquals("El recurso solicitado no existe", ex.getMessage());
    }

    @Test
    void handleGeneral_retornaInternalServerError() {
        Exception ex = new Exception("Error inesperado en el sistema");

        ResponseEntity<Map<String, Object>> respuesta = handler.handleGeneral(ex);

        assertEquals(500, respuesta.getStatusCode().value());
    }
}
