package com.example.eventHub.controller;

import com.example.eventHub.domain.Evento;
import com.example.eventHub.domain.Ingresso;
import com.example.eventHub.domain.Participante;
import com.example.eventHub.dto.IngressoResponse;
import com.example.eventHub.mapper.IngressoMapper;
import com.example.eventHub.service.IngressoService;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(IngressoController.class)
@DisplayName("Testes unitários para IngressoController")
class IngressoControllerTest {

    private static final Long EVENTO_ID = 1L;
    private static final Long PARTICIPANTE_ID = 1L;
    private static final String API_PATH = "/api/ingressos";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IngressoService ingressoService;

    @MockBean
    private IngressoMapper ingressoMapper;

    @Autowired
    private ObjectMapper objectMapper;

    private Evento evento;
    private Participante participante;
    private Ingresso ingresso;
    private IngressoResponse ingressoResponse;

    @BeforeEach
    void setUp() {
        evento = new Evento();
        evento.setId(EVENTO_ID);
        evento.setNome("Tech Conference 2026");
        evento.setData(LocalDateTime.of(2026, 3, 15, 9, 0));
        evento.setLocal("São Paulo");
        evento.setCapacidade(100);

        participante = new Participante();
        participante.setId(PARTICIPANTE_ID);
        participante.setNome("João Silva");
        participante.setEmail("joao@example.com");

        ingresso = new Ingresso();
        ingresso.setId(1L);
        ingresso.setEvento(evento);
        ingresso.setParticipante(participante);
        ingresso.setRegistrationDate(LocalDateTime.now());

        ingressoResponse = new IngressoResponse();
        ingressoResponse.setId(1L);
        IngressoResponse.EventoDTO eventoDTO = new IngressoResponse.EventoDTO(
                EVENTO_ID, "Tech Conference 2026", evento.getData(), "São Paulo", 100
        );
        IngressoResponse.ParticipanteDTO participanteDTO = new IngressoResponse.ParticipanteDTO(
                PARTICIPANTE_ID, "João Silva", "joao@example.com"
        );
        ingressoResponse.setEvento(eventoDTO);
        ingressoResponse.setParticipante(participanteDTO);
    }

    @Test
    @DisplayName("POST /api/ingressos/eventos/{eventId}/participantes/{participantId} - Deve comprar ingresso com sucesso")
    void deveComprarIngressoComSucesso() throws Exception {
        when(ingressoService.comprarIngresso(EVENTO_ID, PARTICIPANTE_ID)).thenReturn(ingresso);
        when(ingressoMapper.toResponse(ingresso)).thenReturn(ingressoResponse);

        mockMvc.perform(post(API_PATH + "/eventos/" + EVENTO_ID + "/participantes/" + PARTICIPANTE_ID))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.evento.nome").value("Tech Conference 2026"))
                .andExpect(jsonPath("$.participante.nome").value("João Silva"));

        verify(ingressoService).comprarIngresso(EVENTO_ID, PARTICIPANTE_ID);
        verify(ingressoMapper).toResponse(ingresso);
    }

    @Test
    @DisplayName("GET /api/ingressos/eventos/{eventId} - Deve listar ingressos do evento")
    void deveListarIngressosDoEvento() throws Exception {
        Participante participante2 = new Participante();
        participante2.setId(2L);
        participante2.setNome("Maria Santos");
        participante2.setEmail("maria@example.com");

        Ingresso ingresso2 = new Ingresso();
        ingresso2.setId(2L);
        ingresso2.setEvento(evento);
        ingresso2.setParticipante(participante2);

        List<Ingresso> ingressos = Arrays.asList(ingresso, ingresso2);
        List<IngressoResponse> responses = Arrays.asList(ingressoResponse);

        when(ingressoService.listarIngressosPorEvento(EVENTO_ID)).thenReturn(ingressos);
        when(ingressoMapper.toResponseList(ingressos)).thenReturn(responses);

        mockMvc.perform(get(API_PATH + "/eventos/" + EVENTO_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].evento.nome").value("Tech Conference 2026"));

        verify(ingressoService).listarIngressosPorEvento(EVENTO_ID);
        verify(ingressoMapper).toResponseList(ingressos);
    }

    @Test
    @DisplayName("GET /api/ingressos/participantes/{participantId} - Deve listar ingressos do participante")
    void deveListarIngressosDoParticipante() throws Exception {
        Evento evento2 = new Evento();
        evento2.setId(2L);
        evento2.setNome("Web Summit 2026");

        Ingresso ingresso2 = new Ingresso();
        ingresso2.setId(2L);
        ingresso2.setEvento(evento2);
        ingresso2.setParticipante(participante);

        List<Ingresso> ingressos = Arrays.asList(ingresso, ingresso2);
        List<IngressoResponse> responses = Arrays.asList(ingressoResponse);

        when(ingressoService.listarIngressosPorParticipante(PARTICIPANTE_ID)).thenReturn(ingressos);
        when(ingressoMapper.toResponseList(ingressos)).thenReturn(responses);

        mockMvc.perform(get(API_PATH + "/participantes/" + PARTICIPANTE_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].participante.nome").value("João Silva"));

        verify(ingressoService).listarIngressosPorParticipante(PARTICIPANTE_ID);
        verify(ingressoMapper).toResponseList(ingressos);
    }

    @Test
    @DisplayName("GET /api/ingressos/eventos/{eventId} - Deve retornar lista vazia quando não há ingressos")
    void deveRetornarListaVaziaQuandoNaoHaIngressos() throws Exception {
        when(ingressoService.listarIngressosPorEvento(EVENTO_ID)).thenReturn(Arrays.asList());
        when(ingressoMapper.toResponseList(Arrays.asList())).thenReturn(Arrays.asList());

        mockMvc.perform(get(API_PATH + "/eventos/" + EVENTO_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)));

        verify(ingressoService).listarIngressosPorEvento(EVENTO_ID);
    }

    @Test
    @DisplayName("GET /api/ingressos/participantes/{participantId} - Deve retornar lista vazia quando participante não tem ingressos")
    void deveRetornarListaVaziaQuandoParticipanteNaoTemIngressos() throws Exception {
        when(ingressoService.listarIngressosPorParticipante(PARTICIPANTE_ID)).thenReturn(Arrays.asList());
        when(ingressoMapper.toResponseList(Arrays.asList())).thenReturn(Arrays.asList());

        mockMvc.perform(get(API_PATH + "/participantes/" + PARTICIPANTE_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)));

        verify(ingressoService).listarIngressosPorParticipante(PARTICIPANTE_ID);
    }

    @Test
    @DisplayName("POST /api/ingressos/eventos/{eventId}/participantes/{participantId} - Deve retornar dados corretos do DTO")
    void deveRetornarDadosCorretosDtoNaCompra() throws Exception {
        when(ingressoService.comprarIngresso(EVENTO_ID, PARTICIPANTE_ID)).thenReturn(ingresso);
        when(ingressoMapper.toResponse(ingresso)).thenReturn(ingressoResponse);

        mockMvc.perform(post(API_PATH + "/eventos/" + EVENTO_ID + "/participantes/" + PARTICIPANTE_ID))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.evento.id").value(EVENTO_ID))
                .andExpect(jsonPath("$.evento.local").value("São Paulo"))
                .andExpect(jsonPath("$.evento.capacidade").value(100))
                .andExpect(jsonPath("$.participante.id").value(PARTICIPANTE_ID))
                .andExpect(jsonPath("$.participante.email").value("joao@example.com"));

        verify(ingressoService).comprarIngresso(EVENTO_ID, PARTICIPANTE_ID);
    }

    @Test
    @DisplayName("GET /api/ingressos/eventos/{eventId} - Deve mapear corretamente de Ingresso para IngressoResponse")
    void deveMapeiaCorretamenteIngressoParaResponse() throws Exception {
        List<Ingresso> ingressos = Arrays.asList(ingresso);
        List<IngressoResponse> responses = Arrays.asList(ingressoResponse);

        when(ingressoService.listarIngressosPorEvento(EVENTO_ID)).thenReturn(ingressos);
        when(ingressoMapper.toResponseList(ingressos)).thenReturn(responses);

        mockMvc.perform(get(API_PATH + "/eventos/" + EVENTO_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].evento.nome").value("Tech Conference 2026"))
                .andExpect(jsonPath("$[0].participante.nome").value("João Silva"));

        verify(ingressoMapper).toResponseList(ingressos);
    }
}

