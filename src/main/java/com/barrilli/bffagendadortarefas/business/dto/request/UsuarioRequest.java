package com.barrilli.bffagendadortarefas.business.dto.request;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UsuarioRequest {
    private String nome;
    private String email;
    private String senha;
    private List<EnderecoRequest> enderecos;
    private List<TelefoneRequest> telefones;
}
