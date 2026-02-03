package com.barrilli.bffagendadortarefas.infra.client;

import com.barrilli.bffagendadortarefas.business.dto.TarefasDTO;
import com.barrilli.bffagendadortarefas.infra.enums.StatusNotificacaoEnum;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@FeignClient(name = "tarefas", url = "${agendador-tarefas.url}")
public interface TarefasClient {

    @PostMapping("/tarefas")
    TarefasDTO gravarTarefa(@RequestHeader("Authorization") String token,
                            @RequestBody TarefasDTO tarefasDTO);

    @GetMapping("/tarefas/eventos")
    List<TarefasDTO> buscaListaTarefasPorPeriodo(
            @RequestHeader("Authorization") String token,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataInicial,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataFinal
    );

    @GetMapping("/tarefas/email")
    List<TarefasDTO> buscaListaTarefasPorEmail(@RequestHeader("Authorization") String token);

    @DeleteMapping("/tarefas")
    void deleterTarefaPorId(@RequestHeader("Authorization") String token, @RequestParam String id);

    @PatchMapping("/tarefas")
    TarefasDTO alteraStatusNotificacao(@RequestHeader("Authorization") String token,
                                       @RequestParam("status") StatusNotificacaoEnum statusNotificacaoEnum,
                                       @RequestParam("id") String id);

    @PutMapping("/tarefas")
    TarefasDTO updateTarefas(@RequestHeader("Authorization") String token,
                             @RequestBody TarefasDTO tarefasDTO,
                             @RequestParam("id") String id);
}
