package com.barrilli.bffagendadortarefas.infra.exception;

import org.springframework.http.HttpStatus;

public class FeignIntegrationException extends RuntimeException {

    private final HttpStatus status;
    private final String responseBody;

    public FeignIntegrationException(HttpStatus status, String responseBody) {
        super(buildMessage(status, responseBody));
        this.status = status;
        this.responseBody = responseBody == null ? "" : responseBody;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getResponseBody() {
        return responseBody;
    }

    private static String buildMessage(HttpStatus status, String responseBody) {
        String body = responseBody == null ? "" : responseBody;
        return "Erro ao chamar serviço externo. Status: "
                + status.value()
                + " "
                + status.getReasonPhrase()
                + ". Corpo: "
                + body;
    }
}
