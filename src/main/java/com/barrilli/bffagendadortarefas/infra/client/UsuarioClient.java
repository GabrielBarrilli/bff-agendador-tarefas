package com.barrilli.bffagendadortarefas.infra.client;

import com.barrilli.bffagendadortarefas.business.dto.EnderecoDTO;
import com.barrilli.bffagendadortarefas.business.dto.TelefoneDTO;
import com.barrilli.bffagendadortarefas.business.dto.UsuarioDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "usuario", url = "${usuario.url}")
public interface UsuarioClient {

    @PostMapping("/login")
    String login(@RequestBody UsuarioDTO dto);

    @PostMapping("/usuario")
    UsuarioDTO salvaUsuario(@RequestBody UsuarioDTO usuarioDTO);

    @GetMapping("/usuario")
    UsuarioDTO buscaUsuarioPorEmail(@RequestParam String email,
                                    @RequestHeader("Authorization") String token);

    @PutMapping("/usuario")
    UsuarioDTO atualizaUsuario(@RequestBody UsuarioDTO usuarioDTO,
                               @RequestHeader("Authorization") String token);

    @GetMapping("/usuario")
    List<UsuarioDTO> buscarTodosUsuarios();

    @DeleteMapping("/usuario/{id}")
    UsuarioDTO deleteUsuarioPorId(@PathVariable Long id,
                                  @RequestHeader("Authorization") String token);

    @DeleteMapping("/usuario/{email}")
    void deleteUsuarioPorEmail(@PathVariable String email,
                               @RequestHeader("Authorization") String token);

    @PutMapping("/usuario/endereco")
    EnderecoDTO atualizaEndereco(@RequestParam("id") Long enderecoId,
                                 @RequestBody EnderecoDTO enderecoDTO,
                                 @RequestHeader("Authorization") String token);

    @PutMapping("/usuario/telefone")
    TelefoneDTO atualizaTelefone(@RequestParam("telefoneId") Long telefoneId,
                                 @RequestBody TelefoneDTO telefoneDTO,
                                 @RequestHeader("Authorization") String token);

    @PostMapping("/usuario/endereco")
    EnderecoDTO cadastraEndereco(@RequestBody EnderecoDTO enderecoDTO,
                                 @RequestHeader("Authorization") String token);

    @PostMapping("/usuario/telefone")
    TelefoneDTO cadastraTelefone(@RequestBody TelefoneDTO telefoneDTO,
                                 @RequestHeader("Authorization") String token);
}
