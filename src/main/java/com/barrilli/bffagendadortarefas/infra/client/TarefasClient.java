package com.barrilli.bffagendadortarefas.infra.client;

import com.barrilli.bffagendadortarefas.business.dto.request.TarefasRequest;
import com.barrilli.bffagendadortarefas.business.dto.response.TarefasResponse;
import com.barrilli.bffagendadortarefas.infra.enums.StatusNotificacaoEnum;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@FeignClient(name = "tarefas", url = "${agendador-tarefas.url}")
public interface TarefasClient {

    @PostMapping("/tarefas")
    TarefasResponse gravarTarefa(@RequestHeader("Authorization") String token,
                                 @RequestBody TarefasRequest tarefasDTO);

    @GetMapping("/tarefas/eventos")
    List<TarefasResponse> buscaListaTarefasPorPeriodo(
            @RequestHeader("Authorization") String token,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataInicial,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataFinal
    );

    @GetMapping("/tarefas/email")
    List<TarefasResponse> buscaListaTarefasPorEmail(@RequestHeader("Authorization") String token);

    @DeleteMapping("/tarefas")
    void deleterTarefaPorId(@RequestHeader("Authorization") String token, @RequestParam String id);

    @PatchMapping("/tarefas")
    TarefasResponse alteraStatusNotificacao(@RequestHeader("Authorization") String token,
                                            @RequestParam("status") StatusNotificacaoEnum statusNotificacaoEnum,
                                            @RequestParam("id") String id);

    @PutMapping("/tarefas")
    TarefasResponse updateTarefas(@RequestHeader("Authorization") String token,
                                  @RequestBody TarefasRequest tarefasDTO,
                                  @RequestParam("id") String id);
}
