package com.example.eventHub.controller;

import com.example.eventHub.domain.Participante;
import com.example.eventHub.service.ParticipanteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ParticipanteController.class)
@DisplayName("Testes unitários para ParticipanteController")
class ParticipanteControllerTest {

    private static final Long PARTICIPANTE_ID = 1L;
    private static final Long PARTICIPANTE_ID_INVALIDO = 999L;
    private static final String API_PATH = "/api/participantes";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ParticipanteService participanteService;

    @Autowired
    private ObjectMapper objectMapper;

    private Participante participante;

    @BeforeEach
    void setUp() {
        participante = new Participante();
        participante.setId(PARTICIPANTE_ID);
        participante.setNome("João Silva");
        participante.setEmail("joao@example.com");
    }

    @Test
    @DisplayName("GET /api/participantes - Deve retornar lista de todos os participantes")
    void deveRetornarTodosOsParticipantes() throws Exception {
        Participante participante2 = new Participante();
        participante2.setId(2L);
        participante2.setNome("Maria Santos");
        participante2.setEmail("maria@example.com");

        List<Participante> participantes = Arrays.asList(participante, participante2);
        when(participanteService.getAllParticipantes()).thenReturn(participantes);

        mockMvc.perform(get(API_PATH))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(PARTICIPANTE_ID))
                .andExpect(jsonPath("$[0].nome").value("João Silva"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].nome").value("Maria Santos"));

        verify(participanteService).getAllParticipantes();
    }

    @Test
    @DisplayName("GET /api/participantes - Deve retornar lista vazia quando não há participantes")
    void deveRetornarListaVaziaQuandoNaoHaParticipantes() throws Exception {
        when(participanteService.getAllParticipantes()).thenReturn(Arrays.asList());

        mockMvc.perform(get(API_PATH))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)));

        verify(participanteService).getAllParticipantes();
    }

    @Test
    @DisplayName("GET /api/participantes/{id} - Deve retornar participante por ID com sucesso")
    void deveRetornarParticipantePorIdComSucesso() throws Exception {
        when(participanteService.getParticipanteById(PARTICIPANTE_ID)).thenReturn(participante);

        mockMvc.perform(get(API_PATH + "/" + PARTICIPANTE_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(PARTICIPANTE_ID))
                .andExpect(jsonPath("$.nome").value("João Silva"))
                .andExpect(jsonPath("$.email").value("joao@example.com"));

        verify(participanteService).getParticipanteById(PARTICIPANTE_ID);
    }

    @Test
    @DisplayName("GET /api/participantes/{id} - Deve retornar 404 quando participante não é encontrado")
    void deveRetornar404QuandoParticipanteNaoEncontrado() throws Exception {
        when(participanteService.getParticipanteById(PARTICIPANTE_ID_INVALIDO)).thenReturn(null);

        mockMvc.perform(get(API_PATH + "/" + PARTICIPANTE_ID_INVALIDO))
                .andExpect(status().isNotFound());

        verify(participanteService).getParticipanteById(PARTICIPANTE_ID_INVALIDO);
    }

    @Test
    @DisplayName("POST /api/participantes - Deve criar novo participante com sucesso")
    void deveCriarNovoParticipanteComSucesso() throws Exception {
        Participante participanteSalvo = new Participante();
        participanteSalvo.setId(PARTICIPANTE_ID);
        participanteSalvo.setNome("João Silva");
        participanteSalvo.setEmail("joao@example.com");

        when(participanteService.createParticipante(any(Participante.class))).thenReturn(participanteSalvo);

        mockMvc.perform(post(API_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(participante)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(PARTICIPANTE_ID))
                .andExpect(jsonPath("$.nome").value("João Silva"));

        verify(participanteService).createParticipante(any(Participante.class));
    }

    @Test
    @DisplayName("PUT /api/participantes/{id} - Deve atualizar participante existente com sucesso")
    void deveAtualizarParticipanteComSucesso() throws Exception {
        Participante participanteAtualizado = new Participante();
        participanteAtualizado.setId(PARTICIPANTE_ID);
        participanteAtualizado.setNome("João Silva - Updated");
        participanteAtualizado.setEmail("joao.silva@example.com");

        when(participanteService.updateParticipante(anyLong(), any(Participante.class))).thenReturn(participanteAtualizado);

        mockMvc.perform(put(API_PATH + "/" + PARTICIPANTE_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(participanteAtualizado)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(PARTICIPANTE_ID))
                .andExpect(jsonPath("$.nome").value("João Silva - Updated"));

        verify(participanteService).updateParticipante(eq(PARTICIPANTE_ID), any(Participante.class));
    }

    @Test
    @DisplayName("PUT /api/participantes/{id} - Deve retornar 404 ao atualizar participante inexistente")
    void deveRetornar404AoAtualizarParticipanteInexistente() throws Exception {
        when(participanteService.updateParticipante(anyLong(), any(Participante.class))).thenReturn(null);

        mockMvc.perform(put(API_PATH + "/" + PARTICIPANTE_ID_INVALIDO)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(participante)))
                .andExpect(status().isNotFound());

        verify(participanteService).updateParticipante(eq(PARTICIPANTE_ID_INVALIDO), any(Participante.class));
    }

    @Test
    @DisplayName("DELETE /api/participantes/{id} - Deve deletar participante com sucesso")
    void deveDeletarParticipanteComSucesso() throws Exception {
        when(participanteService.deleteParticipante(PARTICIPANTE_ID)).thenReturn(true);

        mockMvc.perform(delete(API_PATH + "/" + PARTICIPANTE_ID))
                .andExpect(status().isNoContent());

        verify(participanteService).deleteParticipante(PARTICIPANTE_ID);
    }

    @Test
    @DisplayName("DELETE /api/participantes/{id} - Deve retornar 404 ao deletar participante inexistente")
    void deveRetornar404AoDeletarParticipanteInexistente() throws Exception {
        when(participanteService.deleteParticipante(PARTICIPANTE_ID_INVALIDO)).thenReturn(false);

        mockMvc.perform(delete(API_PATH + "/" + PARTICIPANTE_ID_INVALIDO))
                .andExpect(status().isNotFound());

        verify(participanteService).deleteParticipante(PARTICIPANTE_ID_INVALIDO);
    }

    @Test
    @DisplayName("POST /api/participantes - Deve validar email obrigatório")
    void deveValidarEmailObrigatorio() throws Exception {
        Participante participanteInvalido = new Participante();
        participanteInvalido.setNome("João Silva");
        // Sem email

        mockMvc.perform(post(API_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(participanteInvalido)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("PUT /api/participantes/{id} - Deve preservar ID na atualização")
    void devePreservarIdNaAtualizacao() throws Exception {
        Participante participanteAtualizado = new Participante();
        participanteAtualizado.setId(PARTICIPANTE_ID);
        participanteAtualizado.setNome("João Silva Updated");
        participanteAtualizado.setEmail("joao.updated@example.com");

        when(participanteService.updateParticipante(PARTICIPANTE_ID, participanteAtualizado)).thenReturn(participanteAtualizado);

        mockMvc.perform(put(API_PATH + "/" + PARTICIPANTE_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(participanteAtualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(PARTICIPANTE_ID));

        verify(participanteService).updateParticipante(eq(PARTICIPANTE_ID), any(Participante.class));
    }
}

