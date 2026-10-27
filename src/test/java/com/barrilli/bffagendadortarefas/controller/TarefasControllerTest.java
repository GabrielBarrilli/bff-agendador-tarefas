package com.barrilli.bffagendadortarefas.controller;

import com.barrilli.bffagendadortarefas.business.dto.request.TarefasRequest;
import com.barrilli.bffagendadortarefas.business.dto.response.TarefasResponse;
import com.barrilli.bffagendadortarefas.infra.client.TarefasClient;
import com.barrilli.bffagendadortarefas.infra.enums.StatusNotificacaoEnum;
import com.barrilli.bffagendadortarefas.infra.exception.FeignIntegrationException;
import com.barrilli.bffagendadortarefas.service.TarefasService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// Controller e service reais; só o cliente Feign é mock, para conferir o que chega ao agendador
@WebMvcTest(TarefasController.class)
@Import(TarefasService.class)
class TarefasControllerTest {

    private static final String AUTH = "Bearer tok";

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    TarefasClient tarefasClient;

    @Test
    void criaTarefaRepassandoTokenEData() throws Exception {
        when(tarefasClient.gravarTarefa(eq(AUTH), any())).thenReturn(TarefasResponse.builder().id("t1").build());

        mockMvc.perform(post("/tarefas").header("Authorization", AUTH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nomeTarefa\":\"Ligar\",\"dataEvento\":\"10-01-2026 15:00:00\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("t1"));

        var request = ArgumentCaptor.forClass(TarefasRequest.class);
        verify(tarefasClient).gravarTarefa(eq(AUTH), request.capture());
        assertThat(request.getValue().getDataEvento()).isEqualTo(LocalDateTime.of(2026, 1, 10, 15, 0));
    }

    @Test
    void listaTarefasDoUsuario() throws Exception {
        when(tarefasClient.buscaListaTarefasPorEmail(AUTH)).thenReturn(List.of(
                TarefasResponse.builder().id("t1").dataEvento(LocalDateTime.of(2026, 1, 10, 15, 0)).build()));

        mockMvc.perform(get("/tarefas/email").header("Authorization", AUTH))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("t1"))
                .andExpect(jsonPath("$[0].dataEvento").value("10-01-2026 15:00:00"));
    }

    @Test
    void deletaTarefa() throws Exception {
        mockMvc.perform(delete("/tarefas").param("id", "t1").header("Authorization", AUTH))
                .andExpect(status().isOk());

        verify(tarefasClient).deleterTarefaPorId(AUTH, "t1");
    }

    @Test
    void alteraStatus() throws Exception {
        when(tarefasClient.alteraStatusNotificacao(AUTH, StatusNotificacaoEnum.CANCELADO, "t1"))
                .thenReturn(TarefasResponse.builder().id("t1").statusNotificacao(StatusNotificacaoEnum.CANCELADO).build());

        mockMvc.perform(patch("/tarefas").param("status", "CANCELADO").param("id", "t1").header("Authorization", AUTH))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusNotificacao").value("CANCELADO"));
    }

    @Test
    void atualizaTarefa() throws Exception {
        when(tarefasClient.updateTarefas(eq(AUTH), any(), eq("t1")))
                .thenReturn(TarefasResponse.builder().id("t1").nomeTarefa("Novo").build());

        mockMvc.perform(put("/tarefas").param("id", "t1").header("Authorization", AUTH)
                        .contentType(MediaType.APPLICATION_JSON).content("{\"nomeTarefa\":\"Novo\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("t1"))
                .andExpect(jsonPath("$.nomeTarefa").value("Novo"));

        var request = ArgumentCaptor.forClass(TarefasRequest.class);
        verify(tarefasClient).updateTarefas(eq(AUTH), request.capture(), eq("t1"));
        assertThat(request.getValue().getNomeTarefa()).isEqualTo("Novo");
    }

    @Test
    void erroDoAgendadorVoltaComOMesmoStatus() throws Exception {
        doThrow(new FeignIntegrationException(HttpStatus.NOT_FOUND, "{\"message\":\"Tarefa não encontrada: t1\"}"))
                .when(tarefasClient).deleterTarefaPorId(AUTH, "t1");

        mockMvc.perform(delete("/tarefas").param("id", "t1").header("Authorization", AUTH))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.detalhe").value("{\"message\":\"Tarefa não encontrada: t1\"}"));
    }
}
