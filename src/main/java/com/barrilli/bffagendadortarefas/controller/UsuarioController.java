package com.barrilli.bffagendadortarefas.controller;

import com.barrilli.bffagendadortarefas.business.dto.EnderecoDTO;
import com.barrilli.bffagendadortarefas.business.dto.TelefoneDTO;
import com.barrilli.bffagendadortarefas.business.dto.UsuarioDTO;
import com.barrilli.bffagendadortarefas.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/usuario")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @PostMapping("/login")
    public String login(@RequestBody UsuarioDTO dto) {
        return usuarioService.login(dto);
    }

    @PostMapping
    public UsuarioDTO salvaUsuario(@RequestBody UsuarioDTO usuarioDTO) {
        return usuarioService.salvar(usuarioDTO);
    }

    @PutMapping
    public UsuarioDTO atualizaUsuario(@RequestBody UsuarioDTO usuarioDTO,
                                      @RequestHeader("Authorization") String token) {
        return usuarioService.atualizarUsuario(token, usuarioDTO);
    }

    @GetMapping
    public List<UsuarioDTO> buscarTodosUsuarios() {
        return usuarioService.buscarTodosUsuarios();
    }

    @GetMapping("/email")
    public UsuarioDTO buscaUsuarioPorEmail(@RequestParam String email,
                                           @RequestHeader("Authorization") String token) {
        return usuarioService.findUsuarioByEmail(email, token);
    }

    @DeleteMapping("/{id}")
    public UsuarioDTO deleteUsuarioPorId(@PathVariable Long id,
                                         @RequestHeader("Authorization") String token) {
        return usuarioService.deleteById(id, token);
    }

    @DeleteMapping("/{email}")
    public void deleteUsuarioPorEmail(@PathVariable String email,
                                      @RequestHeader("Authorization") String token) {
        usuarioService.deleteByEmail(email, token);
    }

    @PutMapping("/endereco")
    public EnderecoDTO atualizaEndereco(@RequestParam("id") Long enderecoId,
                                        @RequestBody EnderecoDTO enderecoDTO,
                                        @RequestHeader("Authorization") String token) {
        return usuarioService.atualizarEndereco(enderecoId, enderecoDTO, token);
    }

    @PutMapping("/telefone")
    public TelefoneDTO atualizaTelefone(@RequestParam("telefoneId") Long telefoneId,
                                        @RequestBody TelefoneDTO telefoneDTO,
                                        @RequestHeader("Authorization") String token) {
        return usuarioService.atualizarTelefone(telefoneId, telefoneDTO, token);
    }

    @PostMapping("/endereco")
    public EnderecoDTO cadastraEndereco(@RequestBody EnderecoDTO enderecoDTO,
                                        @RequestHeader("Authorization") String token) {
        return usuarioService.cadastraEndereco(token, enderecoDTO);
    }

    @PostMapping("/telefone")
    public TelefoneDTO cadastraTelefone(@RequestBody TelefoneDTO telefoneDTO,
                                        @RequestHeader("Authorization") String token) {
        return usuarioService.cadastraTelefone(token, telefoneDTO);
    }
}
