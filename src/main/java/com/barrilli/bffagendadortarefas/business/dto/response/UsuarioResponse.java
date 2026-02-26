package com.barrilli.bffagendadortarefas.business.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UsuarioResponse {
    private String nome;
    private String email;
    private List<EnderecoResponse> enderecos;
    private List<TelefoneResponse> telefones;
}
