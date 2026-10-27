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

    @PostMapping("/v1/tarefas")
    TarefasResponse gravarTarefa(@RequestHeader("Authorization") String token,
                                 @RequestBody TarefasRequest tarefasDTO);

    @GetMapping("/v1/tarefas/email")
    List<TarefasResponse> buscaListaTarefasPorEmail(@RequestHeader("Authorization") String token);

    @DeleteMapping("/v1/tarefas")
    void deleterTarefaPorId(@RequestHeader("Authorization") String token, @RequestParam String id);

    @PatchMapping("/v1/tarefas")
    TarefasResponse alteraStatusNotificacao(@RequestHeader("Authorization") String token,
                                            @RequestParam("status") StatusNotificacaoEnum statusNotificacaoEnum,
                                            @RequestParam("id") String id);

    @PutMapping("/v1/tarefas")
    TarefasResponse updateTarefas(@RequestHeader("Authorization") String token,
                                  @RequestBody TarefasRequest tarefasDTO,
                                  @RequestParam("id") String id);

    @GetMapping("/internal/tarefas/pendentes")
    List<TarefasResponse> buscaTarefasPendentes(
            @RequestHeader("X-Internal-Key") String internalKey,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fim
    );

    @PatchMapping("/internal/tarefas/{id}/notificada")
    TarefasResponse marcaComoNotificada(@RequestHeader("X-Internal-Key") String internalKey,
                                        @PathVariable String id);
}
