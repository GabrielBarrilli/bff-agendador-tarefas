package com.barrilli.bffagendadortarefas.infra.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class FeignExceptionHandler {

    @ExceptionHandler(FeignIntegrationException.class)
    public ResponseEntity<Map<String, Object>> handleFeignException(FeignIntegrationException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("mensagem", "Erro ao comunicar com serviço externo");
        body.put("status", ex.getStatus().value());

        if (!ex.getResponseBody().isBlank()) {
            body.put("detalhe", ex.getResponseBody());
        }

        return ResponseEntity.status(ex.getStatus()).body(body);
    }
}
