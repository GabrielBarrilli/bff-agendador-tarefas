package com.barrilli.bffagendadortarefas.service;

import com.barrilli.bffagendadortarefas.business.dto.EnderecoDTO;
import com.barrilli.bffagendadortarefas.business.dto.TelefoneDTO;
import com.barrilli.bffagendadortarefas.business.dto.UsuarioDTO;
import com.barrilli.bffagendadortarefas.infra.client.UsuarioClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioClient usuarioClient;

    public UsuarioDTO salvar(UsuarioDTO usuarioDTO) {
        return usuarioClient.salvaUsuario(usuarioDTO);
    }

    public String login(UsuarioDTO dto) {
        return usuarioClient.login(dto);
    }

    public UsuarioDTO findUsuarioByEmail(String email, String token) {
        return usuarioClient.buscaUsuarioPorEmail(email, token);
    }

    public List<UsuarioDTO> buscarTodosUsuarios() {
        return usuarioClient.buscarTodosUsuarios();
    }

    public UsuarioDTO deleteById(Long id, String token) {
        return usuarioClient.deleteUsuarioPorId(id, token);
    }

    public void deleteByEmail(String email, String token) {
        usuarioClient.deleteUsuarioPorEmail(email, token);
    }

    public UsuarioDTO atualizarUsuario(String token, UsuarioDTO usuarioDTO) {
        return usuarioClient.atualizaUsuario(usuarioDTO, token);
    }

    public EnderecoDTO atualizarEndereco(Long enderecoId, EnderecoDTO enderecoDTO, String token) {
        return usuarioClient.atualizaEndereco(enderecoId, enderecoDTO, token);
    }

    public TelefoneDTO atualizarTelefone(Long telefoneId, TelefoneDTO telefoneDTO, String token) {
        return usuarioClient.atualizaTelefone(telefoneId, telefoneDTO, token);
    }

    public EnderecoDTO cadastraEndereco(String token, EnderecoDTO enderecoDTO) {
        return usuarioClient.cadastraEndereco(enderecoDTO, token);
    }

    public TelefoneDTO cadastraTelefone(String token, TelefoneDTO telefoneDTO) {
        return usuarioClient.cadastraTelefone(telefoneDTO, token);
    }
}
