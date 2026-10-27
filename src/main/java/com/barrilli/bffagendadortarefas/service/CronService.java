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
        LocalDateTime proximaHoraMaisCinco = LocalDateTime.now().plusHours(1).plusMinutes(5);

        List<TarefasResponse> listaTarefas =
                tarefasService.buscaTarefasPendentes(internalApiKey, proximaHora, proximaHoraMaisCinco);

        log.info("{} tarefas encontradas", listaTarefas.size());

        listaTarefas.forEach(tarefa -> {
            emailService.enviarEmail(tarefa);
            tarefasService.marcaComoNotificada(internalApiKey, tarefa.getId());
            log.info("A tarefa {} foi notificada com sucesso", tarefa.getId());
        });
    }
}
