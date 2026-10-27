package com.barrilli.bffagendadortarefas.infra.exception;

import feign.Request;
import feign.Response;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.nio.charset.StandardCharsets;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class FeignErrorTest {

    private final FeignError decoder = new FeignError();

    private static Response resposta(int status, String body) {
        var request = Request.create(Request.HttpMethod.GET, "http://usuario/v1/usuario", Map.of(), null, StandardCharsets.UTF_8, null);
        var builder = Response.builder().status(status).reason("x").request(request).headers(Map.of());
        return body == null ? builder.build() : builder.body(body, StandardCharsets.UTF_8).build();
    }

    @Test
    void erro4xxViraFeignIntegrationExceptionComCorpo() {
        var ex = decoder.decode("UsuarioClient#login", resposta(401, "{\"message\":\"Usuário ou senha inválidos\"}"));

        assertThat(ex).isInstanceOf(FeignIntegrationException.class);
        var feignEx = (FeignIntegrationException) ex;
        assertThat(feignEx.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(feignEx.getResponseBody()).contains("Usuário ou senha inválidos");
        assertThat(feignEx.getMessage()).contains("401 Unauthorized");
    }

    @Test
    void erro5xxSemCorpoViraFeignIntegrationExceptionComCorpoVazio() {
        var ex = (FeignIntegrationException) decoder.decode("EmailClient#enviarEmail", resposta(503, null));

        assertThat(ex.getStatus()).isEqualTo(HttpStatus.SERVICE_UNAVAILABLE);
        assertThat(ex.getResponseBody()).isEmpty();
    }

    @Test
    void statusDesconhecidoUsaODecoderPadrao() {
        var ex = decoder.decode("UsuarioClient#login", resposta(599, ""));

        assertThat(ex).isNotInstanceOf(FeignIntegrationException.class);
    }
}
