package com.barrilli.bffagendadortarefas.controller;

import com.barrilli.bffagendadortarefas.business.dto.response.EnderecoResponse;
import com.barrilli.bffagendadortarefas.business.dto.response.TelefoneResponse;
import com.barrilli.bffagendadortarefas.business.dto.response.UsuarioResponse;
import com.barrilli.bffagendadortarefas.infra.client.UsuarioClient;
import com.barrilli.bffagendadortarefas.infra.exception.FeignIntegrationException;
import com.barrilli.bffagendadortarefas.service.UsuarioService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// Controller e service reais; só o cliente Feign é mock, para conferir o que chega ao serviço de usuário
@WebMvcTest(UsuarioController.class)
@Import(UsuarioService.class)
class UsuarioControllerTest {

    private static final String AUTH = "Bearer tok";
    private static final String JSON = MediaType.APPLICATION_JSON_VALUE;

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    UsuarioClient usuarioClient;

    @Test
    void loginDevolveOToken() throws Exception {
        when(usuarioClient.login(any())).thenReturn(AUTH);

        mockMvc.perform(post("/v1/usuario/login").contentType(JSON).content("{\"email\":\"ana@t.com\",\"senha\":\"s3nha\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string(AUTH));
    }

    @Test
    void loginComSenhaErradaDevolve401() throws Exception {
        when(usuarioClient.login(any())).thenThrow(new FeignIntegrationException(HttpStatus.UNAUTHORIZED, ""));

        mockMvc.perform(post("/v1/usuario/login").contentType(JSON).content("{\"email\":\"ana@t.com\",\"senha\":\"x\"}"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.mensagem").value("Erro ao comunicar com serviço externo"))
                .andExpect(jsonPath("$.detalhe").doesNotExist());
    }

    @Test
    void cadastroDevolveUsuario() throws Exception {
        when(usuarioClient.salvaUsuario(any())).thenReturn(UsuarioResponse.builder().email("ana@t.com").build());

        mockMvc.perform(post("/v1/usuario").contentType(JSON).content("{\"nome\":\"Ana\",\"email\":\"ana@t.com\",\"senha\":\"s3nha\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("ana@t.com"));
    }

    @Test
    void cadastroDuplicadoDevolve409() throws Exception {
        when(usuarioClient.salvaUsuario(any()))
                .thenThrow(new FeignIntegrationException(HttpStatus.CONFLICT, "{\"message\":\"Email já cadastrado\"}"));

        mockMvc.perform(post("/v1/usuario").contentType(JSON).content("{\"email\":\"ana@t.com\"}"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409));
    }

    @Test
    void buscaPorEmail() throws Exception {
        when(usuarioClient.buscaUsuarioPorEmail("ana@t.com", AUTH)).thenReturn(UsuarioResponse.builder().email("ana@t.com").build());

        mockMvc.perform(get("/v1/usuario/email").param("email", "ana@t.com").header("Authorization", AUTH))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("ana@t.com"));
    }

    @Test
    void atualizaUsuario() throws Exception {
        when(usuarioClient.atualizaUsuario(any(), eq(AUTH))).thenReturn(UsuarioResponse.builder().nome("Ana B").build());

        mockMvc.perform(put("/v1/usuario").header("Authorization", AUTH).contentType(JSON).content("{\"nome\":\"Ana B\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Ana B"));
    }

    @Test
    void deletaPorIdNumerico() throws Exception {
        when(usuarioClient.deleteUsuarioPorId(7L, AUTH)).thenReturn(UsuarioResponse.builder().email("ana@t.com").build());

        mockMvc.perform(delete("/v1/usuario/7").header("Authorization", AUTH))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("ana@t.com"));

        verify(usuarioClient).deleteUsuarioPorId(7L, AUTH);
    }

    @Test
    void deletaPorEmail() throws Exception {
        mockMvc.perform(delete("/v1/usuario/email/ana@t.com").header("Authorization", AUTH))
                .andExpect(status().isOk());

        verify(usuarioClient).deleteUsuarioPorEmail("ana@t.com", AUTH);
    }

    @Test
    void cadastraEAtualizaEndereco() throws Exception {
        when(usuarioClient.cadastraEndereco(any(), eq(AUTH))).thenReturn(EnderecoResponse.builder().id(1L).build());
        when(usuarioClient.atualizaEndereco(eq(1L), any(), eq(AUTH))).thenReturn(EnderecoResponse.builder().id(1L).cidade("Recife").build());

        mockMvc.perform(post("/v1/usuario/endereco").header("Authorization", AUTH).contentType(JSON).content("{\"rua\":\"A\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
        mockMvc.perform(put("/v1/usuario/endereco").param("id", "1").header("Authorization", AUTH).contentType(JSON).content("{\"cidade\":\"Recife\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cidade").value("Recife"));
    }

    @Test
    void cadastraEAtualizaTelefone() throws Exception {
        when(usuarioClient.cadastraTelefone(any(), eq(AUTH))).thenReturn(TelefoneResponse.builder().id(2L).build());
        when(usuarioClient.atualizaTelefone(eq(2L), any(), eq(AUTH))).thenReturn(TelefoneResponse.builder().id(2L).ddd("81").build());

        mockMvc.perform(post("/v1/usuario/telefone").header("Authorization", AUTH).contentType(JSON).content("{\"numero\":\"999\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2));
        mockMvc.perform(put("/v1/usuario/telefone").param("telefoneId", "2").header("Authorization", AUTH).contentType(JSON).content("{\"ddd\":\"81\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ddd").value("81"));
    }
}
