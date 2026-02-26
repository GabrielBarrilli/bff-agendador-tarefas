package com.barrilli.bffagendadortarefas.infra.config;

import com.barrilli.bffagendadortarefas.infra.exception.FeignError;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {

    @Bean
    public ErrorDecoder feignErrorDecoder() {
        return new FeignError();
    }
}
