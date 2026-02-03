package com.barrilli.bffagendadortarefas.controller;

import com.barrilli.bffagendadortarefas.business.dto.EnderecoDTO;
import com.barrilli.bffagendadortarefas.business.dto.TelefoneDTO;
import com.barrilli.bffagendadortarefas.business.dto.UsuarioDTO;
import com.barrilli.bffagendadortarefas.infra.security.SecurityConfig;
import com.barrilli.bffagendadortarefas.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/usuario")
@RequiredArgsConstructor
@Tag(name = "Usuario", description = "Cadastro e login de usuários")
@SecurityRequirement(name = SecurityConfig.SECURITY_SCHEME)
public class UsuarioController {

    private final UsuarioService usuarioService;

    @PostMapping("/login")
    @Operation(summary = "Login de usuário", description = "Login de usuários")
    @ApiResponse(responseCode = "200", description = "Usuário logado com sucesso")
    @ApiResponse(responseCode = "400", description = "Credenciais inválidas")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    public String login(@RequestBody UsuarioDTO dto) {
        return usuarioService.login(dto);
    }

    @PostMapping
    @Operation(summary = "Salvar usuários", description = "Endpoint para salvar novos usuários")
    @ApiResponse(responseCode = "200", description = "Usuário salvo com sucesso")
    @ApiResponse(responseCode = "400", description = "Usuário já existe")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    public UsuarioDTO salvaUsuario(@RequestBody UsuarioDTO usuarioDTO) {
        return usuarioService.salvar(usuarioDTO);
    }

    @PostMapping("/endereco")
    @Operation(summary = "Cadastrar endereço", description = "Adiciona um endereço ao usuário autenticado")
    @ApiResponse(responseCode = "200", description = "Endereço cadastrado com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados inválidos ou endereço já cadastrado")
    @ApiResponse(responseCode = "401", description = "Token de autorização ausente ou inválido")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    public EnderecoDTO cadastraEndereco(@RequestBody EnderecoDTO enderecoDTO,
                                        @RequestHeader(name = "Authorization", required = false) String token) {
        return usuarioService.cadastraEndereco(token, enderecoDTO);
    }

    @PostMapping("/telefone")
    @Operation(summary = "Cadastrar telefone", description = "Adiciona um telefone ao usuário autenticado")
    @ApiResponse(responseCode = "200", description = "Telefone cadastrado com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados inválidos ou telefone já cadastrado")
    @ApiResponse(responseCode = "401", description = "Token de autorização ausente ou inválido")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    public TelefoneDTO cadastraTelefone(@RequestBody TelefoneDTO telefoneDTO,
                                        @RequestHeader(name = "Authorization", required = false) String token) {
        return usuarioService.cadastraTelefone(token, telefoneDTO);
    }

    @GetMapping
    @Operation(summary = "Buscar todos os usuários", description = "Endpoint para buscar todos os usuários")
    @ApiResponse(responseCode = "200", description = "Usuários buscados com sucesso")
    @ApiResponse(responseCode = "400", description = "Requisição inválida")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    public List<UsuarioDTO> buscarTodosUsuarios() {
        return usuarioService.buscarTodosUsuarios();
    }

    @GetMapping("/email")
    @Operation(summary = "Buscar usuário por e-mail", description = "Recupera um usuário pelo e-mail informado")
    @ApiResponse(responseCode = "200", description = "Usuário encontrado")
    @ApiResponse(responseCode = "401", description = "Token de autorização ausente ou inválido")
    @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    public UsuarioDTO buscaUsuarioPorEmail(@RequestParam String email,
                                           @RequestHeader(name = "Authorization", required = false) String token) {
        return usuarioService.findUsuarioByEmail(email, token);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar usuário por ID", description = "Remove permanentemente o usuário identificado pelo ID")
    @ApiResponse(responseCode = "200", description = "Usuário deletado com sucesso")
    @ApiResponse(responseCode = "401", description = "Token de autorização ausente ou inválido")
    @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    public UsuarioDTO deleteUsuarioPorId(@PathVariable Long id,
                                         @RequestHeader(name = "Authorization", required = false) String token) {
        return usuarioService.deleteById(id, token);
    }

    @DeleteMapping("/{email}")
    @Operation(summary = "Deletar usuário por e-mail", description = "Remove o usuário a partir do e-mail informado")
    @ApiResponse(responseCode = "200", description = "Usuário deletado com sucesso")
    @ApiResponse(responseCode = "401", description = "Token de autorização ausente ou inválido")
    @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    public void deleteUsuarioPorEmail(@PathVariable String email,
                                      @RequestHeader(name = "Authorization", required = false) String token) {
        usuarioService.deleteByEmail(email, token);
    }

    @PutMapping
    @Operation(summary = "Atualizar usuário", description = "Atualiza os dados do usuário autenticado")
    @ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados inválidos")
    @ApiResponse(responseCode = "401", description = "Token de autorização ausente ou inválido")
    @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    public UsuarioDTO atualizaUsuario(@RequestBody UsuarioDTO usuarioDTO,
                                      @RequestHeader(name = "Authorization", required = false) String token) {
        return usuarioService.atualizarUsuario(token, usuarioDTO);
    }

    @PutMapping("/endereco")
    @Operation(summary = "Atualizar endereço", description = "Atualiza um endereço existente do usuário autenticado")
    @ApiResponse(responseCode = "200", description = "Endereço atualizado com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados inválidos")
    @ApiResponse(responseCode = "401", description = "Token de autorização ausente ou inválido")
    @ApiResponse(responseCode = "404", description = "Endereço não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    public EnderecoDTO atualizaEndereco(@RequestParam("id") Long enderecoId,
                                        @RequestBody EnderecoDTO enderecoDTO,
                                        @RequestHeader(name = "Authorization", required = false) String token) {
        return usuarioService.atualizarEndereco(enderecoId, enderecoDTO, token);
    }

    @PutMapping("/telefone")
    @Operation(summary = "Atualizar telefone", description = "Atualiza um telefone existente do usuário autenticado")
    @ApiResponse(responseCode = "200", description = "Telefone atualizado com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados inválidos")
    @ApiResponse(responseCode = "401", description = "Token de autorização ausente ou inválido")
    @ApiResponse(responseCode = "404", description = "Telefone não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    public TelefoneDTO atualizaTelefone(@RequestParam("telefoneId") Long telefoneId,
                                        @RequestBody TelefoneDTO telefoneDTO,
                                        @RequestHeader(name = "Authorization", required = false) String token) {
        return usuarioService.atualizarTelefone(telefoneId, telefoneDTO, token);
    }
}
