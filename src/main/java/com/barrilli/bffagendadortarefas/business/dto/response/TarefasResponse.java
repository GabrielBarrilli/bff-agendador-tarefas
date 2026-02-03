package com.barrilli.bffagendadortarefas.business.dto.response;

import com.barrilli.bffagendadortarefas.infra.enums.StatusNotificacaoEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TarefasResponse {
    private String id;
    private String nomeTarefa;
    private String descricao;
    private String emailUsuario;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime dataEvento;
    private StatusNotificacaoEnum statusNotificacao;
}
