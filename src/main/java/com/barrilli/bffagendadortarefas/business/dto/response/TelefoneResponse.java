package com.barrilli.bffagendadortarefas.business.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TelefoneResponse {
    private Long id;
    private String numero;
    private String ddd;
}
