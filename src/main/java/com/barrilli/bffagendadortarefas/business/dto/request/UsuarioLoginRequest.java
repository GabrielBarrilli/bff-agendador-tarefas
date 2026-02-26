package com.barrilli.bffagendadortarefas.business.dto.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UsuarioLoginRequest {
    private String email;
    private String senha;
}
