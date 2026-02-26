package com.barrilli.bffagendadortarefas.service;

import com.barrilli.bffagendadortarefas.business.dto.response.TarefasResponse;
import com.barrilli.bffagendadortarefas.infra.client.EmailClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final EmailClient emailClient;

    public void enviarEmail(TarefasResponse tarefasDTO) {
        emailClient.enviarEmail(tarefasDTO);
    }
}
