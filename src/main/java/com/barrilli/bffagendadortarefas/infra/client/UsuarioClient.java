package com.barrilli.bffagendadortarefas.infra.client;

import com.barrilli.bffagendadortarefas.business.dto.request.EnderecoRequest;
import com.barrilli.bffagendadortarefas.business.dto.request.TelefoneRequest;
import com.barrilli.bffagendadortarefas.business.dto.request.UsuarioLoginRequest;
import com.barrilli.bffagendadortarefas.business.dto.request.UsuarioRequest;
import com.barrilli.bffagendadortarefas.business.dto.response.EnderecoResponse;
import com.barrilli.bffagendadortarefas.business.dto.response.TelefoneResponse;
import com.barrilli.bffagendadortarefas.business.dto.response.UsuarioResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "usuario", url = "${usuario.url}")
public interface UsuarioClient {

    @PostMapping("/login")
    String login(@RequestBody UsuarioLoginRequest dto);

    @PostMapping("/usuario")
    UsuarioResponse salvaUsuario(@RequestBody UsuarioRequest usuarioDTO);

    @GetMapping("/usuario")
    UsuarioResponse buscaUsuarioPorEmail(@RequestParam String email,
                                         @RequestHeader("Authorization") String token);

    @PutMapping("/usuario")
    UsuarioResponse atualizaUsuario(@RequestBody UsuarioRequest usuarioDTO,
                                    @RequestHeader("Authorization") String token);

    @GetMapping("/usuario")
    List<UsuarioResponse> buscarTodosUsuarios();

    @DeleteMapping("/usuario/{id}")
    UsuarioResponse deleteUsuarioPorId(@PathVariable Long id,
                                       @RequestHeader("Authorization") String token);

    @DeleteMapping("/usuario/{email}")
    void deleteUsuarioPorEmail(@PathVariable String email,
                               @RequestHeader("Authorization") String token);

    @PutMapping("/usuario/endereco")
    EnderecoResponse atualizaEndereco(@RequestParam("id") Long enderecoId,
                                      @RequestBody EnderecoRequest enderecoDTO,
                                      @RequestHeader("Authorization") String token);

    @PutMapping("/usuario/telefone")
    TelefoneResponse atualizaTelefone(@RequestParam("telefoneId") Long telefoneId,
                                      @RequestBody TelefoneRequest telefoneDTO,
                                      @RequestHeader("Authorization") String token);

    @PostMapping("/usuario/endereco")
    EnderecoResponse cadastraEndereco(@RequestBody EnderecoRequest enderecoDTO,
                                      @RequestHeader("Authorization") String token);

    @PostMapping("/usuario/telefone")
    TelefoneResponse cadastraTelefone(@RequestBody TelefoneRequest telefoneDTO,
                                      @RequestHeader("Authorization") String token);
}
