package com.barrilli.bffagendadortarefas.service;

import com.barrilli.bffagendadortarefas.business.dto.request.EnderecoRequest;
import com.barrilli.bffagendadortarefas.business.dto.request.TelefoneRequest;
import com.barrilli.bffagendadortarefas.business.dto.request.UsuarioLoginRequest;
import com.barrilli.bffagendadortarefas.business.dto.request.UsuarioRequest;
import com.barrilli.bffagendadortarefas.business.dto.response.EnderecoResponse;
import com.barrilli.bffagendadortarefas.business.dto.response.TelefoneResponse;
import com.barrilli.bffagendadortarefas.business.dto.response.UsuarioResponse;
import com.barrilli.bffagendadortarefas.infra.client.UsuarioClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioClient usuarioClient;

    public UsuarioResponse salvar(UsuarioRequest usuarioDTO) {
        return usuarioClient.salvaUsuario(usuarioDTO);
    }

    public String login(UsuarioLoginRequest dto) {
        return usuarioClient.login(dto);
    }

    public UsuarioResponse findUsuarioByEmail(String email, String token) {
        return usuarioClient.buscaUsuarioPorEmail(email, token);
    }

    public UsuarioResponse deleteById(Long id, String token) {
        return usuarioClient.deleteUsuarioPorId(id, token);
    }

    public void deleteByEmail(String email, String token) {
        usuarioClient.deleteUsuarioPorEmail(email, token);
    }

    public UsuarioResponse atualizarUsuario(String token, UsuarioRequest usuarioDTO) {
        return usuarioClient.atualizaUsuario(usuarioDTO, token);
    }

    public EnderecoResponse atualizarEndereco(Long enderecoId, EnderecoRequest enderecoDTO, String token) {
        return usuarioClient.atualizaEndereco(enderecoId, enderecoDTO, token);
    }

    public TelefoneResponse atualizarTelefone(Long telefoneId, TelefoneRequest telefoneDTO, String token) {
        return usuarioClient.atualizaTelefone(telefoneId, telefoneDTO, token);
    }

    public EnderecoResponse cadastraEndereco(String token, EnderecoRequest enderecoDTO) {
        return usuarioClient.cadastraEndereco(enderecoDTO, token);
    }

    public TelefoneResponse cadastraTelefone(String token, TelefoneRequest telefoneDTO) {
        return usuarioClient.cadastraTelefone(telefoneDTO, token);
    }
}
