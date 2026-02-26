package com.barrilli.bffagendadortarefas.infra.client;

import com.barrilli.bffagendadortarefas.business.dto.response.TarefasResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "notificacao", url = "${notificacao.url}")
public interface EmailClient {

    @PostMapping
    void enviarEmail(@RequestBody TarefasResponse tarefasDTO);
}
