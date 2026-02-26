package com.barrilli.bffagendadortarefas.business.dto.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TelefoneRequest {
    private String numero;
    private String ddd;
}
