package com.example.eventHub.controller;

import com.example.eventHub.domain.Evento;
import com.example.eventHub.service.EventoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EventoController.class)
@DisplayName("Testes unitários para EventoController")
class EventoControllerTest {

    private static final Long EVENTO_ID = 1L;
    private static final Long EVENTO_ID_INVALIDO = 999L;
    private static final String API_PATH = "/api/eventos";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EventoService eventoService;

    @Autowired
    private ObjectMapper objectMapper;

    private Evento evento;

    @BeforeEach
    void setUp() {
        evento = new Evento();
        evento.setId(EVENTO_ID);
        evento.setNome("Tech Conference 2026");
        evento.setData(LocalDateTime.of(2026, 3, 15, 9, 0));
        evento.setLocal("São Paulo");
        evento.setCapacidade(100);
    }

    @Test
    @DisplayName("GET /api/eventos - Deve retornar lista de todos os eventos")
    void deveRetornarTodosOsEventos() throws Exception {
        Evento evento2 = new Evento();
        evento2.setId(2L);
        evento2.setNome("Web Summit 2026");

        List<Evento> eventos = Arrays.asList(evento, evento2);
        when(eventoService.getAllEventos()).thenReturn(eventos);

        mockMvc.perform(get(API_PATH))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(EVENTO_ID))
                .andExpect(jsonPath("$[0].nome").value("Tech Conference 2026"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].nome").value("Web Summit 2026"));

        verify(eventoService).getAllEventos();
    }

    @Test
    @DisplayName("GET /api/eventos - Deve retornar lista vazia quando não há eventos")
    void deveRetornarListaVaziaQuandoNaoHaEventos() throws Exception {
        when(eventoService.getAllEventos()).thenReturn(Arrays.asList());

        mockMvc.perform(get(API_PATH))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)));

        verify(eventoService).getAllEventos();
    }

    @Test
    @DisplayName("GET /api/eventos/{id} - Deve retornar evento por ID com sucesso")
    void deveRetornarEventoPorIdComSucesso() throws Exception {
        when(eventoService.getEventoById(EVENTO_ID)).thenReturn(evento);

        mockMvc.perform(get(API_PATH + "/" + EVENTO_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(EVENTO_ID))
                .andExpect(jsonPath("$.nome").value("Tech Conference 2026"))
                .andExpect(jsonPath("$.local").value("São Paulo"))
                .andExpect(jsonPath("$.capacidade").value(100));

        verify(eventoService).getEventoById(EVENTO_ID);
    }

    @Test
    @DisplayName("GET /api/eventos/{id} - Deve retornar 404 quando evento não é encontrado")
    void deveRetornar404QuandoEventoNaoEncontrado() throws Exception {
        when(eventoService.getEventoById(EVENTO_ID_INVALIDO)).thenReturn(null);

        mockMvc.perform(get(API_PATH + "/" + EVENTO_ID_INVALIDO))
                .andExpect(status().isNotFound());

        verify(eventoService).getEventoById(EVENTO_ID_INVALIDO);
    }

    @Test
    @DisplayName("POST /api/eventos - Deve criar novo evento com sucesso")
    void deveCriarNovoEventoComSucesso() throws Exception {
        evento.setId(null);
        Evento eventoSalvo = new Evento();
        eventoSalvo.setId(EVENTO_ID);
        eventoSalvo.setNome("Tech Conference 2026");
        eventoSalvo.setData(LocalDateTime.of(2026, 3, 15, 9, 0));
        eventoSalvo.setLocal("São Paulo");
        eventoSalvo.setCapacidade(100);

        when(eventoService.createEvento(any(Evento.class))).thenReturn(eventoSalvo);

        mockMvc.perform(post(API_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(evento)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(EVENTO_ID))
                .andExpect(jsonPath("$.nome").value("Tech Conference 2026"));

        verify(eventoService).createEvento(any(Evento.class));
    }

    @Test
    @DisplayName("PUT /api/eventos/{id} - Deve atualizar evento existente com sucesso")
    void deveAtualizarEventoComSucesso() throws Exception {
        Evento eventoAtualizado = new Evento();
        eventoAtualizado.setId(EVENTO_ID);
        eventoAtualizado.setNome("Tech Conference 2026 - Updated");
        eventoAtualizado.setData(LocalDateTime.of(2026, 4, 15, 9, 0));
        eventoAtualizado.setLocal("Rio de Janeiro");
        eventoAtualizado.setCapacidade(150);

        when(eventoService.updateEvento(anyLong(), any(Evento.class))).thenReturn(eventoAtualizado);

        mockMvc.perform(put(API_PATH + "/" + EVENTO_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(eventoAtualizado)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(EVENTO_ID))
                .andExpect(jsonPath("$.nome").value("Tech Conference 2026 - Updated"))
                .andExpect(jsonPath("$.capacidade").value(150));

        verify(eventoService).updateEvento(eq(EVENTO_ID), any(Evento.class));
    }

    @Test
    @DisplayName("PUT /api/eventos/{id} - Deve retornar 404 ao atualizar evento inexistente")
    void deveRetornar404AoAtualizarEventoInexistente() throws Exception {
        when(eventoService.updateEvento(anyLong(), any(Evento.class))).thenReturn(null);

        mockMvc.perform(put(API_PATH + "/" + EVENTO_ID_INVALIDO)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(evento)))
                .andExpect(status().isNotFound());

        verify(eventoService).updateEvento(eq(EVENTO_ID_INVALIDO), any(Evento.class));
    }

    @Test
    @DisplayName("DELETE /api/eventos/{id} - Deve deletar evento com sucesso")
    void deveDeletarEventoComSucesso() throws Exception {
        when(eventoService.deleteEvento(EVENTO_ID)).thenReturn(true);

        mockMvc.perform(delete(API_PATH + "/" + EVENTO_ID))
                .andExpect(status().isNoContent());

        verify(eventoService).deleteEvento(EVENTO_ID);
    }

    @Test
    @DisplayName("DELETE /api/eventos/{id} - Deve retornar 404 ao deletar evento inexistente")
    void deveRetornar404AoDeletarEventoInexistente() throws Exception {
        when(eventoService.deleteEvento(EVENTO_ID_INVALIDO)).thenReturn(false);

        mockMvc.perform(delete(API_PATH + "/" + EVENTO_ID_INVALIDO))
                .andExpect(status().isNotFound());

        verify(eventoService).deleteEvento(EVENTO_ID_INVALIDO);
    }

    @Test
    @DisplayName("POST /api/eventos - Deve validar campos obrigatórios")
    void deveValidarCamposObrigatorios() throws Exception {
        Evento eventoInvalido = new Evento();
        // Não preenchendo campos obrigatórios

        mockMvc.perform(post(API_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(eventoInvalido)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("PUT /api/eventos/{id} - Deve preservar ID na atualização")
    void devePreservarIdNaAtualizacao() throws Exception {
        Evento eventoAtualizado = new Evento();
        eventoAtualizado.setId(EVENTO_ID);
        eventoAtualizado.setNome("Updated Name");
        eventoAtualizado.setData(LocalDateTime.of(2026, 4, 15, 9, 0));
        eventoAtualizado.setLocal("Rio");
        eventoAtualizado.setCapacidade(150);

        when(eventoService.updateEvento(EVENTO_ID, eventoAtualizado)).thenReturn(eventoAtualizado);

        mockMvc.perform(put(API_PATH + "/" + EVENTO_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(eventoAtualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(EVENTO_ID));

        verify(eventoService).updateEvento(eq(EVENTO_ID), any(Evento.class));
    }
}

