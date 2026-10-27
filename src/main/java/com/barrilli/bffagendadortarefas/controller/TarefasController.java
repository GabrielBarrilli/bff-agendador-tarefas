package com.barrilli.bffagendadortarefas.controller;

import com.barrilli.bffagendadortarefas.business.dto.request.TarefasRequest;
import com.barrilli.bffagendadortarefas.business.dto.response.TarefasResponse;
import com.barrilli.bffagendadortarefas.infra.enums.StatusNotificacaoEnum;
import com.barrilli.bffagendadortarefas.infra.security.SecurityConfig;
import com.barrilli.bffagendadortarefas.service.TarefasService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tarefas")
@RequiredArgsConstructor
@Tag(name = "Tarefas", description = "Gestão de tarefas e notificações")
@SecurityRequirement(name = SecurityConfig.SECURITY_SCHEME)
public class TarefasController {

    private final TarefasService tarefasService;

    @PostMapping
    @Operation(summary = "Criar tarefa", description = "Cria uma nova tarefa para o usuário autenticado")
    @ApiResponse(responseCode = "200", description = "Tarefa criada com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados inválidos para criação da tarefa")
    @ApiResponse(responseCode = "401", description = "Token de autorização ausente ou inválido")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    public TarefasResponse gravarTarefa(@RequestHeader(name = "Authorization", required = false) String token,
                                        @RequestBody TarefasRequest tarefasDTO) {
        return tarefasService.gravarTarefa(token, tarefasDTO);
    }

    @GetMapping("/email")
    @Operation(summary = "Listar tarefas por usuário", description = "Retorna tarefas associadas ao token informado")
    @ApiResponse(responseCode = "200", description = "Tarefas retornadas com sucesso")
    @ApiResponse(responseCode = "401", description = "Token de autorização ausente ou inválido")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    public List<TarefasResponse> buscaListaTarefasPorEmail(@RequestHeader(name = "Authorization", required = false) String token) {
        return tarefasService.buscaTarefasPorEmail(token);
    }

    @DeleteMapping
    @Operation(summary = "Excluir tarefa", description = "Remove uma tarefa a partir do ID informado")
    @ApiResponse(responseCode = "204", description = "Tarefa deletada com sucesso")
    @ApiResponse(responseCode = "400", description = "ID inválido")
    @ApiResponse(responseCode = "404", description = "Tarefa não encontrada")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    public void deleterTarefaPorId(@RequestHeader(name = "Authorization", required = false) String token,
                                   @RequestParam String id) {
        tarefasService.deletarTarefaPorId(token, id);
    }

    @PatchMapping
    @Operation(summary = "Alterar status de notificação", description = "Atualiza o status de notificação da tarefa")
    @ApiResponse(responseCode = "200", description = "Status atualizado com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados inválidos")
    @ApiResponse(responseCode = "404", description = "Tarefa não encontrada")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    public TarefasResponse alteraStatusNotificacao(
            @RequestHeader(name = "Authorization", required = false) String token,
            @RequestParam("status") StatusNotificacaoEnum statusNotificacaoEnum,
            @RequestParam("id") String id) {
        return tarefasService.alteraStatus(token, statusNotificacaoEnum, id);
    }

    @PutMapping
    @Operation(summary = "Atualizar tarefa", description = "Atualiza os dados de uma tarefa existente")
    @ApiResponse(responseCode = "200", description = "Tarefa atualizada com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados inválidos")
    @ApiResponse(responseCode = "404", description = "Tarefa não encontrada")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    public TarefasResponse updateTarefas(@RequestHeader(name = "Authorization", required = false) String token,
                                         @RequestBody TarefasRequest tarefasDTO,
                                         @RequestParam("id") String id) {
        return tarefasService.updateTarefa(token, tarefasDTO, id);
    }
}
