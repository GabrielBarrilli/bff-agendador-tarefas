package com.barrilli.bffagendadortarefas.infra.exception;

import feign.Response;
import feign.codec.ErrorDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public class FeignError implements ErrorDecoder {

    private static final Logger log = LoggerFactory.getLogger(FeignError.class);
    private final ErrorDecoder defaultDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {
        HttpStatus status = HttpStatus.resolve(response.status());
        if (status == null) {
            return defaultDecoder.decode(methodKey, response);
        }

        if (status.is4xxClientError() || status.is5xxServerError()) {
            String body = readBody(response);
            log.warn("Erro Feign no método {} com status {} e corpo: {}", methodKey, status.value(), body);
            return new FeignIntegrationException(status, body);
        }

        return defaultDecoder.decode(methodKey, response);
    }

    private String readBody(Response response) {
        if (response.body() == null) {
            return "";
        }

        try (var reader = new BufferedReader(new InputStreamReader(response.body().asInputStream(), StandardCharsets.UTF_8))) {
            return reader.lines().collect(Collectors.joining());
        } catch (IOException ex) {
            log.debug("Não foi possível ler o corpo da resposta Feign", ex);
            return "";
        }
    }
}
