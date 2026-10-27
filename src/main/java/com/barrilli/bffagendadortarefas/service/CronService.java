package com.barrilli.bffagendadortarefas.service;

import com.barrilli.bffagendadortarefas.business.dto.response.TarefasResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CronService {

    Logger log = LoggerFactory.getLogger(CronService.class);

    private final TarefasService tarefasService;
    private final EmailService emailService;

    @Value("${internal.api-key}")
    private String internalApiKey;

    @Scheduled(cron = "${cron.horario}")
    public void buscaTarefasProximasDeNotificacaoEEnviaEmail() {
        log.info("Iniciando busca de tarefas");
        LocalDateTime proximaHora = LocalDateTime.now().plusHours(1);
        LocalDateTime proximaHoraMaisCinco = proximaHora.plusMinutes(5);

        List<TarefasResponse> listaTarefas =
                tarefasService.buscaTarefasPendentes(internalApiKey, proximaHora, proximaHoraMaisCinco);

        log.info("{} tarefas encontradas", listaTarefas.size());

        // Uma falha não aborta o lote; a tarefa que falhou fica PENDENTE e só aparece no log
        // ponytail: sem retentativa (a janela seguinte já não inclui a tarefa); buscar PENDENTES atrasadas se isso importar
        listaTarefas.forEach(tarefa -> {
            try {
                emailService.enviarEmail(tarefa);
                tarefasService.marcaComoNotificada(internalApiKey, tarefa.getId());
                log.info("A tarefa {} foi notificada com sucesso", tarefa.getId());
            } catch (RuntimeException e) {
                log.error("Falha ao notificar a tarefa {}", tarefa.getId(), e);
            }
        });
    }
}
