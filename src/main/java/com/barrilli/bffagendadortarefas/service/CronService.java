package com.barrilli.bffagendadortarefas.service;

import com.barrilli.bffagendadortarefas.business.dto.request.UsuarioLoginRequest;
import com.barrilli.bffagendadortarefas.business.dto.response.TarefasResponse;
import com.barrilli.bffagendadortarefas.infra.enums.StatusNotificacaoEnum;
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
    private final UsuarioService usuarioService;

    @Value("${usuario.email}")
    private String usuarioEmail;

    @Value("${usuario.senha}")
    private String usuarioSenha;

    @Scheduled(cron = "${cron.horario}")
    public void buscaTarefasProximasDeNotificacaoEEnviaEmail() {
        var token = login(converterParaRequestDTO());
        log.info("Iniciando busca de tarefas");
        LocalDateTime proximaHora = LocalDateTime.now().plusHours(1);
        LocalDateTime proximaHoraMaisCinco = LocalDateTime.now().plusHours(1).plusMinutes(5);

        List<TarefasResponse> listaTarefas =
                tarefasService.buscaTarefasAgendadasPorPeriodo(token, proximaHora, proximaHoraMaisCinco);

        log.info("{} tarefas encontradas para usuário {}", listaTarefas.size(), usuarioEmail);

        listaTarefas.forEach(tarefa -> {
            emailService.enviarEmail(tarefa);
            tarefasService.alteraStatus(token, StatusNotificacaoEnum.NOTIFICADO, tarefa.getId());
            log.info("A tarefa {} do usuário {} foi notificada com sucesso", tarefa, usuarioEmail);
        });
    }

    public String login(UsuarioLoginRequest usuarioDTO) {
        return usuarioService.login(usuarioDTO);
    }

    private UsuarioLoginRequest converterParaRequestDTO() {
        return UsuarioLoginRequest.builder()
                .email(usuarioEmail)
                .senha(usuarioSenha)
                .build();
    }
}
