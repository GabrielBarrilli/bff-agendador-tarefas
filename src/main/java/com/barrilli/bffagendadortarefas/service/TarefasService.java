package com.barrilli.bffagendadortarefas.service;

import com.barrilli.bffagendadortarefas.business.dto.TarefasDTO;
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

    public TarefasDTO gravarTarefa(String token, TarefasDTO tarefasDTO) {
        return tarefasClient.gravarTarefa(token, tarefasDTO);
    }

    public List<TarefasDTO> buscaTarefasAgendadasPorPeriodo(String token, LocalDateTime dataInicial, LocalDateTime dataFinal) {
        return tarefasClient.buscaListaTarefasPorPeriodo(token, dataInicial, dataFinal);
    }

    public List<TarefasDTO> buscaTarefasPorEmail(String token) {
        return tarefasClient.buscaListaTarefasPorEmail(token);
    }

    public void deletarTarefaPorId(String token, String id) {
        tarefasClient.deleterTarefaPorId(token, id);
    }

    public TarefasDTO alteraStatus(String token, StatusNotificacaoEnum statusNotificacao, String id) {
        return tarefasClient.alteraStatusNotificacao(token, statusNotificacao, id);
    }

    public TarefasDTO updateTarefa(String token, TarefasDTO tarefasDTO, String id) {
        return tarefasClient.updateTarefas(token, tarefasDTO, id);
    }
}
