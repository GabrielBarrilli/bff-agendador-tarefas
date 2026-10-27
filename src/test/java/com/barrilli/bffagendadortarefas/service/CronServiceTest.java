package com.barrilli.bffagendadortarefas.service;

import com.barrilli.bffagendadortarefas.business.dto.response.TarefasResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CronServiceTest {

    private static final String KEY = "chave-interna";

    @Mock
    TarefasService tarefasService;

    @Mock
    EmailService emailService;

    @InjectMocks
    CronService cronService;

    private final TarefasResponse t1 = TarefasResponse.builder().id("t1").build();
    private final TarefasResponse t2 = TarefasResponse.builder().id("t2").build();

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(cronService, "internalApiKey", KEY);
    }

    @Test
    void enviaLembreteEMarcaCadaTarefaComoNotificada() {
        when(tarefasService.buscaTarefasPendentes(eq(KEY), any(), any())).thenReturn(List.of(t1, t2));

        cronService.buscaTarefasProximasDeNotificacaoEEnviaEmail();

        verify(emailService).enviarEmail(t1);
        verify(emailService).enviarEmail(t2);
        verify(tarefasService).marcaComoNotificada(KEY, "t1");
        verify(tarefasService).marcaComoNotificada(KEY, "t2");
    }

    @Test
    void buscaAsTarefasDaProximaHoraNumaJanelaDeCincoMinutos() {
        var antes = LocalDateTime.now();

        cronService.buscaTarefasProximasDeNotificacaoEEnviaEmail();

        var inicio = ArgumentCaptor.forClass(LocalDateTime.class);
        var fim = ArgumentCaptor.forClass(LocalDateTime.class);
        verify(tarefasService).buscaTarefasPendentes(eq(KEY), inicio.capture(), fim.capture());
        assertThat(inicio.getValue()).isBetween(antes.plusHours(1), LocalDateTime.now().plusHours(1));
        assertThat(fim.getValue()).isEqualTo(inicio.getValue().plusMinutes(5));
    }

    @Test
    void umEmailQueFalhaNaoImpedeOsOutros() {
        when(tarefasService.buscaTarefasPendentes(eq(KEY), any(), any())).thenReturn(List.of(t1, t2));
        doThrow(new RuntimeException("SMTP fora do ar")).when(emailService).enviarEmail(t1);

        cronService.buscaTarefasProximasDeNotificacaoEEnviaEmail();

        verify(emailService).enviarEmail(t2);
        verify(tarefasService).marcaComoNotificada(KEY, "t2");
        verify(tarefasService, never()).marcaComoNotificada(KEY, "t1");
    }
}
