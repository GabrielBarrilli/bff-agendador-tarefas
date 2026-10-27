package com.barrilli.bffagendadortarefas.service;

import com.barrilli.bffagendadortarefas.business.dto.request.TarefasRequest;
import com.barrilli.bffagendadortarefas.business.dto.response.TarefasResponse;
import com.barrilli.bffagendadortarefas.infra.client.TarefasClient;
import com.barrilli.bffagendadortarefas.infra.enums.StatusNotificacaoEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TarefasService {

    private final TarefasClient tarefasClient;

    public TarefasResponse gravarTarefa(String token, TarefasRequest tarefasDTO) {
        return tarefasClient.gravarTarefa(token, tarefasDTO);
    }

    public List<TarefasResponse> buscaTarefasPendentes(String internalKey, LocalDateTime inicio, LocalDateTime fim) {
        return tarefasClient.buscaTarefasPendentes(internalKey, inicio, fim);
    }

    public void marcaComoNotificada(String internalKey, String id) {
        tarefasClient.marcaComoNotificada(internalKey, id);
    }

    public List<TarefasResponse> buscaTarefasPorEmail(String token) {
        return tarefasClient.buscaListaTarefasPorEmail(token);
    }

    public void deletarTarefaPorId(String token, String id) {
        tarefasClient.deleterTarefaPorId(token, id);
    }

    public TarefasResponse alteraStatus(String token, StatusNotificacaoEnum statusNotificacao, String id) {
        return tarefasClient.alteraStatusNotificacao(token, statusNotificacao, id);
    }

    public TarefasResponse updateTarefa(String token, TarefasRequest tarefasDTO, String id) {
        return tarefasClient.updateTarefas(token, tarefasDTO, id);
    }
}
