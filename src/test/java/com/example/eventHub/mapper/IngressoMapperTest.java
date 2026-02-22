package com.example.eventHub.mapper;

import com.example.eventHub.domain.Evento;
import com.example.eventHub.domain.Ingresso;
import com.example.eventHub.domain.Participante;
import com.example.eventHub.dto.IngressoResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Testes unitários para IngressoMapper")
class IngressoMapperTest {

    private static final Long EVENTO_ID = 1L;
    private static final Long PARTICIPANTE_ID = 1L;
    private static final Long INGRESSO_ID = 1L;
    private static final String NOME_EVENTO = "Tech Conference 2026";
    private static final String LOCAL_EVENTO = "São Paulo";
    private static final String NOME_PARTICIPANTE = "João Silva";
    private static final String EMAIL_PARTICIPANTE = "joao@example.com";
    private static final int CAPACIDADE = 100;

    private IngressoMapper ingressoMapper;
    private Evento evento;
    private Participante participante;
    private Ingresso ingresso;

    @BeforeEach
    void setUp() {
        ingressoMapper = new IngressoMapper();

        evento = new Evento();
        evento.setId(EVENTO_ID);
        evento.setNome(NOME_EVENTO);
        evento.setData(LocalDateTime.of(2026, 3, 15, 9, 0));
        evento.setLocal(LOCAL_EVENTO);
        evento.setCapacidade(CAPACIDADE);

        participante = new Participante();
        participante.setId(PARTICIPANTE_ID);
        participante.setNome(NOME_PARTICIPANTE);
        participante.setEmail(EMAIL_PARTICIPANTE);

        ingresso = new Ingresso();
        ingresso.setId(INGRESSO_ID);
        ingresso.setEvento(evento);
        ingresso.setParticipante(participante);
    }

    @Test
    @DisplayName("Deve converter Ingresso para IngressoResponse com sucesso")
    void deveConverterIngressoParaResponse() {
        IngressoResponse response = ingressoMapper.toResponse(ingresso);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(INGRESSO_ID);
        assertThat(response.getEvento()).isNotNull();
        assertThat(response.getEvento().getId()).isEqualTo(EVENTO_ID);
        assertThat(response.getEvento().getNome()).isEqualTo(NOME_EVENTO);
        assertThat(response.getEvento().getLocal()).isEqualTo(LOCAL_EVENTO);
        assertThat(response.getEvento().getCapacidade()).isEqualTo(CAPACIDADE);
        assertThat(response.getParticipante()).isNotNull();
        assertThat(response.getParticipante().getId()).isEqualTo(PARTICIPANTE_ID);
        assertThat(response.getParticipante().getNome()).isEqualTo(NOME_PARTICIPANTE);
        assertThat(response.getParticipante().getEmail()).isEqualTo(EMAIL_PARTICIPANTE);
    }

    @Test
    @DisplayName("Deve retornar null quando Ingresso é null")
    void deveRetornarNullQuandoIngressoNull() {
        IngressoResponse response = ingressoMapper.toResponse(null);

        assertThat(response).isNull();
    }

    @Test
    @DisplayName("Deve converter lista de Ingressos para lista de IngressoResponse")
    void deveConverterListaDeIngressos() {
        Ingresso ingresso2 = new Ingresso();
        ingresso2.setId(2L);
        ingresso2.setEvento(evento);

        Participante participante2 = new Participante();
        participante2.setId(2L);
        participante2.setNome("Maria Santos");
        participante2.setEmail("maria@example.com");
        ingresso2.setParticipante(participante2);

        List<Ingresso> ingressos = Arrays.asList(ingresso, ingresso2);

        List<IngressoResponse> responses = ingressoMapper.toResponseList(ingressos);

        assertThat(responses).isNotNull().hasSize(2);
        assertThat(responses.get(0).getId()).isEqualTo(INGRESSO_ID);
        assertThat(responses.get(0).getParticipante().getNome()).isEqualTo(NOME_PARTICIPANTE);
        assertThat(responses.get(1).getId()).isEqualTo(2L);
        assertThat(responses.get(1).getParticipante().getNome()).isEqualTo("Maria Santos");
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando lista de Ingressos é null")
    void deveRetornarListaVaziaQuandoListaNull() {
        List<IngressoResponse> responses = ingressoMapper.toResponseList(null);

        assertThat(responses).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando lista de Ingressos está vazia")
    void deveRetornarListaVaziaQuandoListaVazia() {
        List<IngressoResponse> responses = ingressoMapper.toResponseList(List.of());

        assertThat(responses).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("Deve mapear corretamente todos os campos do evento")
    void deveMapearcamposDoEvento() {
        LocalDateTime dataEvento = LocalDateTime.of(2026, 12, 31, 20, 30);
        evento.setData(dataEvento);

        IngressoResponse response = ingressoMapper.toResponse(ingresso);

        assertThat(response.getEvento().getData()).isEqualTo(dataEvento);
        assertThat(response.getEvento().getId()).isEqualTo(EVENTO_ID);
        assertThat(response.getEvento().getNome()).isEqualTo(NOME_EVENTO);
        assertThat(response.getEvento().getLocal()).isEqualTo(LOCAL_EVENTO);
        assertThat(response.getEvento().getCapacidade()).isEqualTo(CAPACIDADE);
    }

    @Test
    @DisplayName("Deve mapear corretamente todos os campos do participante")
    void deveMapearcamposDoParticipante() {
        IngressoResponse response = ingressoMapper.toResponse(ingresso);

        assertThat(response.getParticipante().getId()).isEqualTo(PARTICIPANTE_ID);
        assertThat(response.getParticipante().getNome()).isEqualTo(NOME_PARTICIPANTE);
        assertThat(response.getParticipante().getEmail()).isEqualTo(EMAIL_PARTICIPANTE);
    }

    @Test
    @DisplayName("Deve converter múltiplos ingressos mantendo a ordem")
    void deveConverterMultiplosIngressosManetndoOrdem() {
        Ingresso ingresso1 = criarIngresso(1L, "Participante 1");
        Ingresso ingresso2 = criarIngresso(2L, "Participante 2");
        Ingresso ingresso3 = criarIngresso(3L, "Participante 3");

        List<Ingresso> ingressos = Arrays.asList(ingresso1, ingresso2, ingresso3);
        List<IngressoResponse> responses = ingressoMapper.toResponseList(ingressos);

        assertThat(responses).hasSize(3);
        assertThat(responses.get(0).getId()).isEqualTo(1L);
        assertThat(responses.get(1).getId()).isEqualTo(2L);
        assertThat(responses.get(2).getId()).isEqualTo(3L);
        assertThat(responses.get(0).getParticipante().getNome()).isEqualTo("Participante 1");
        assertThat(responses.get(1).getParticipante().getNome()).isEqualTo("Participante 2");
        assertThat(responses.get(2).getParticipante().getNome()).isEqualTo("Participante 3");
    }

    private Ingresso criarIngresso(Long id, String nomeParticipante) {
        Participante p = new Participante();
        p.setId(id);
        p.setNome(nomeParticipante);
        p.setEmail(nomeParticipante.toLowerCase().replace(" ", "") + "@example.com");

        Ingresso i = new Ingresso();
        i.setId(id);
        i.setEvento(evento);
        i.setParticipante(p);

        return i;
    }
}

