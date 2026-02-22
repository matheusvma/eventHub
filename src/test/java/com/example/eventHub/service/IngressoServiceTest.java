package com.example.eventHub.service;

import com.example.eventHub.domain.Evento;
import com.example.eventHub.domain.Ingresso;
import com.example.eventHub.domain.Participante;
import com.example.eventHub.exception.EventoLotadoException;
import com.example.eventHub.exception.EventoNaoEncontradoException;
import com.example.eventHub.exception.ParticipanteJaInscritoException;
import com.example.eventHub.exception.ParticipanteNaoEncontradoException;
import com.example.eventHub.repository.IngressoRepository;
import com.example.eventHub.validator.IngressoValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes unitários para IngressoService")
class IngressoServiceTest {

    private static final Long EVENTO_ID = 1L;
    private static final Long PARTICIPANTE_ID = 1L;
    private static final Long EVENTO_ID_INVALIDO = 999L;
    private static final Long PARTICIPANTE_ID_INVALIDO = 999L;
    private static final int CAPACIDADE_INICIAL = 100;

    @Mock
    private IngressoRepository ingressoRepository;

    @Mock
    private EventoService eventoService;

    @Mock
    private ParticipanteService participanteService;

    @Mock
    private IngressoValidator ingressoValidator;

    @InjectMocks
    private IngressoService ingressoService;

    private Evento evento;
    private Participante participante;
    private Ingresso ingresso;

    @BeforeEach
    void setUp() {
        evento = new Evento();
        evento.setId(EVENTO_ID);
        evento.setNome("Tech Conference 2026");
        evento.setData(LocalDateTime.of(2026, 3, 15, 9, 0));
        evento.setLocal("São Paulo");
        evento.setCapacidade(CAPACIDADE_INICIAL);

        participante = new Participante();
        participante.setId(PARTICIPANTE_ID);
        participante.setNome("João Silva");
        participante.setEmail("joao@example.com");

        ingresso = new Ingresso();
        ingresso.setId(EVENTO_ID);
        ingresso.setEvento(evento);
        ingresso.setParticipante(participante);
    }

    @Test
    @DisplayName("Deve comprar ingresso com sucesso quando evento tem capacidade disponível")
    void deveComprarIngressoComSucesso() {
        when(eventoService.getEventoById(EVENTO_ID)).thenReturn(evento);
        when(participanteService.getParticipanteById(PARTICIPANTE_ID)).thenReturn(participante);
        doNothing().when(ingressoValidator).validaCompraIngresso(evento, participante, EVENTO_ID, PARTICIPANTE_ID);
        when(ingressoRepository.save(any(Ingresso.class))).thenReturn(ingresso);
        when(eventoService.updateEvento(anyLong(), any(Evento.class))).thenReturn(evento);

        Ingresso resultado = ingressoService.comprarIngresso(EVENTO_ID, PARTICIPANTE_ID);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getEvento()).isEqualTo(evento);
        assertThat(resultado.getParticipante()).isEqualTo(participante);

        verify(eventoService).getEventoById(EVENTO_ID);
        verify(participanteService).getParticipanteById(PARTICIPANTE_ID);
        verify(ingressoValidator).validaCompraIngresso(evento, participante, EVENTO_ID, PARTICIPANTE_ID);
        verify(ingressoRepository).save(any(Ingresso.class));
        verify(eventoService).updateEvento(eq(EVENTO_ID), any(Evento.class));
    }

    @Test
    @DisplayName("Deve decrementar a capacidade do evento após compra")
    void deveDecrementarCapacidadeDoEvento() {
        int capacidadeInicial = evento.getCapacidade();
        when(eventoService.getEventoById(EVENTO_ID)).thenReturn(evento);
        when(participanteService.getParticipanteById(PARTICIPANTE_ID)).thenReturn(participante);
        doNothing().when(ingressoValidator).validaCompraIngresso(evento, participante, EVENTO_ID, PARTICIPANTE_ID);
        when(ingressoRepository.save(any(Ingresso.class))).thenReturn(ingresso);
        when(eventoService.updateEvento(anyLong(), any(Evento.class))).thenReturn(evento);

        ingressoService.comprarIngresso(EVENTO_ID, PARTICIPANTE_ID);

        assertThat(evento.getCapacidade()).isEqualTo(capacidadeInicial - 1);
        verify(eventoService).updateEvento(eq(EVENTO_ID), eq(evento));
    }

    @Test
    @DisplayName("Deve lançar exceção quando evento não é encontrado")
    void deveLancarExcecaoQuandoEventoNaoEncontrado() {
        when(eventoService.getEventoById(EVENTO_ID_INVALIDO)).thenReturn(null);
        when(participanteService.getParticipanteById(PARTICIPANTE_ID)).thenReturn(participante);
        doThrow(new EventoNaoEncontradoException(EVENTO_ID_INVALIDO))
                .when(ingressoValidator).validaCompraIngresso(null, participante, EVENTO_ID_INVALIDO, PARTICIPANTE_ID);

        assertThatThrownBy(() -> ingressoService.comprarIngresso(EVENTO_ID_INVALIDO, PARTICIPANTE_ID))
                .isInstanceOf(EventoNaoEncontradoException.class)
                .hasMessageContaining("Evento com ID 999 não foi encontrado");

        verify(eventoService).getEventoById(EVENTO_ID_INVALIDO);
        verify(participanteService).getParticipanteById(PARTICIPANTE_ID);
        verify(ingressoValidator).validaCompraIngresso(null, participante, EVENTO_ID_INVALIDO, PARTICIPANTE_ID);
        verify(ingressoRepository, never()).save(any(Ingresso.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando participante não é encontrado")
    void deveLancarExcecaoQuandoParticipanteNaoEncontrado() {
        when(eventoService.getEventoById(EVENTO_ID)).thenReturn(evento);
        when(participanteService.getParticipanteById(PARTICIPANTE_ID_INVALIDO)).thenReturn(null);
        doThrow(new ParticipanteNaoEncontradoException(PARTICIPANTE_ID_INVALIDO))
                .when(ingressoValidator).validaCompraIngresso(evento, null, EVENTO_ID, PARTICIPANTE_ID_INVALIDO);

        assertThatThrownBy(() -> ingressoService.comprarIngresso(EVENTO_ID, PARTICIPANTE_ID_INVALIDO))
                .isInstanceOf(ParticipanteNaoEncontradoException.class)
                .hasMessageContaining("Participante com ID 999 não foi encontrado");

        verify(eventoService).getEventoById(EVENTO_ID);
        verify(participanteService).getParticipanteById(PARTICIPANTE_ID_INVALIDO);
        verify(ingressoValidator).validaCompraIngresso(evento, null, EVENTO_ID, PARTICIPANTE_ID_INVALIDO);
        verify(ingressoRepository, never()).save(any(Ingresso.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando evento está lotado")
    void deveLancarExcecaoQuandoEventoLotado() {
        evento.setCapacidade(1);
        when(eventoService.getEventoById(EVENTO_ID)).thenReturn(evento);
        when(participanteService.getParticipanteById(PARTICIPANTE_ID)).thenReturn(participante);
        doThrow(new EventoLotadoException(EVENTO_ID, 1))
                .when(ingressoValidator).validaCompraIngresso(evento, participante, EVENTO_ID, PARTICIPANTE_ID);

        assertThatThrownBy(() -> ingressoService.comprarIngresso(EVENTO_ID, PARTICIPANTE_ID))
                .isInstanceOf(EventoLotadoException.class)
                .hasMessageContaining("Evento com ID 1 está lotado")
                .hasMessageContaining("Capacidade máxima: 1");

        verify(eventoService).getEventoById(EVENTO_ID);
        verify(participanteService).getParticipanteById(PARTICIPANTE_ID);
        verify(ingressoValidator).validaCompraIngresso(evento, participante, EVENTO_ID, PARTICIPANTE_ID);
        verify(ingressoRepository, never()).save(any(Ingresso.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando participante já está inscrito")
    void deveLancarExcecaoQuandoParticipanteJaInscrito() {
        when(eventoService.getEventoById(EVENTO_ID)).thenReturn(evento);
        when(participanteService.getParticipanteById(PARTICIPANTE_ID)).thenReturn(participante);
        doThrow(new ParticipanteJaInscritoException(PARTICIPANTE_ID, EVENTO_ID))
                .when(ingressoValidator).validaCompraIngresso(evento, participante, EVENTO_ID, PARTICIPANTE_ID);

        assertThatThrownBy(() -> ingressoService.comprarIngresso(EVENTO_ID, PARTICIPANTE_ID))
                .isInstanceOf(ParticipanteJaInscritoException.class)
                .hasMessageContaining("Participante com ID 1 já está inscrito no evento com ID 1");

        verify(eventoService).getEventoById(EVENTO_ID);
        verify(participanteService).getParticipanteById(PARTICIPANTE_ID);
        verify(ingressoValidator).validaCompraIngresso(evento, participante, EVENTO_ID, PARTICIPANTE_ID);
        verify(ingressoRepository, never()).save(any(Ingresso.class));
    }

    @Test
    @DisplayName("Deve permitir múltiplas compras até atingir a capacidade")
    void devePermitirMultiplasComprasAteCapacidade() {
        evento.setCapacidade(3);

        Participante participante2 = new Participante();
        participante2.setId(2L);
        participante2.setNome("Maria Santos");

        Participante participante3 = new Participante();
        participante3.setId(3L);
        participante3.setNome("Pedro Costa");

        when(eventoService.getEventoById(EVENTO_ID)).thenReturn(evento);
        when(participanteService.getParticipanteById(PARTICIPANTE_ID)).thenReturn(participante);
        when(participanteService.getParticipanteById(2L)).thenReturn(participante2);
        when(participanteService.getParticipanteById(3L)).thenReturn(participante3);
        when(ingressoRepository.save(any(Ingresso.class))).thenReturn(ingresso);
        when(eventoService.updateEvento(anyLong(), any(Evento.class))).thenReturn(evento);
        doNothing().when(ingressoValidator).validaCompraIngresso(any(), any(), anyLong(), anyLong());

        ingressoService.comprarIngresso(EVENTO_ID, PARTICIPANTE_ID);
        assertThat(evento.getCapacidade()).isEqualTo(2);

        ingressoService.comprarIngresso(EVENTO_ID, 2L);
        assertThat(evento.getCapacidade()).isOne();

        ingressoService.comprarIngresso(EVENTO_ID, 3L);
        assertThat(evento.getCapacidade()).isZero();

        verify(ingressoRepository, times(3)).save(any(Ingresso.class));
        verify(eventoService, times(3)).updateEvento(eq(EVENTO_ID), eq(evento));
    }

    @Test
    @DisplayName("Deve listar todos os ingressos de um participante")
    void deveListarIngressosDeUmParticipante() {
        Ingresso ingresso1 = new Ingresso();
        ingresso1.setId(EVENTO_ID);
        ingresso1.setParticipante(participante);
        ingresso1.setEvento(evento);

        Evento evento2 = new Evento();
        evento2.setId(2L);
        evento2.setNome("Web Summit 2026");

        Ingresso ingresso2 = new Ingresso();
        ingresso2.setId(2L);
        ingresso2.setParticipante(participante);
        ingresso2.setEvento(evento2);

        List<Ingresso> ingressosEsperados = Arrays.asList(ingresso1, ingresso2);
        when(ingressoRepository.findByParticipanteId(PARTICIPANTE_ID)).thenReturn(ingressosEsperados);

        List<Ingresso> resultado = ingressoService.listarIngressosPorParticipante(PARTICIPANTE_ID);

        assertThat(resultado).isNotNull().hasSize(2);
        assertThat(resultado).containsExactlyInAnyOrder(ingresso1, ingresso2);
        assertThat(resultado.get(0).getParticipante()).isEqualTo(participante);

        verify(ingressoRepository).findByParticipanteId(PARTICIPANTE_ID);
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando participante não tem ingressos")
    void deveRetornarListaVaziaQuandoParticipanteNaoTemIngressos() {
        when(ingressoRepository.findByParticipanteId(PARTICIPANTE_ID)).thenReturn(Arrays.asList());

        List<Ingresso> resultado = ingressoService.listarIngressosPorParticipante(PARTICIPANTE_ID);

        assertThat(resultado).isNotNull().isEmpty();

        verify(ingressoRepository).findByParticipanteId(PARTICIPANTE_ID);
    }

    @Test
    @DisplayName("Deve validar ordem de execução das operações na compra")
    void deveValidarOrdemDeExecucaoNaCompra() {
        when(eventoService.getEventoById(EVENTO_ID)).thenReturn(evento);
        when(participanteService.getParticipanteById(PARTICIPANTE_ID)).thenReturn(participante);
        doNothing().when(ingressoValidator).validaCompraIngresso(evento, participante, EVENTO_ID, PARTICIPANTE_ID);
        when(ingressoRepository.save(any(Ingresso.class))).thenReturn(ingresso);
        when(eventoService.updateEvento(anyLong(), any(Evento.class))).thenReturn(evento);

        ingressoService.comprarIngresso(EVENTO_ID, PARTICIPANTE_ID);

        var inOrder = inOrder(eventoService, participanteService, ingressoValidator, ingressoRepository, eventoService);
        inOrder.verify(eventoService).getEventoById(EVENTO_ID);
        inOrder.verify(participanteService).getParticipanteById(PARTICIPANTE_ID);
        inOrder.verify(ingressoValidator).validaCompraIngresso(evento, participante, EVENTO_ID, PARTICIPANTE_ID);
        inOrder.verify(ingressoRepository).save(any(Ingresso.class));
        inOrder.verify(eventoService).updateEvento(eq(EVENTO_ID), any(Evento.class));
    }

    @Test
    @DisplayName("Deve criar ingresso com evento e participante corretos")
    void deveCriarIngressoComDadosCorretos() {
        when(eventoService.getEventoById(EVENTO_ID)).thenReturn(evento);
        when(participanteService.getParticipanteById(PARTICIPANTE_ID)).thenReturn(participante);
        doNothing().when(ingressoValidator).validaCompraIngresso(evento, participante, EVENTO_ID, PARTICIPANTE_ID);
        when(ingressoRepository.save(any(Ingresso.class))).thenAnswer(invocation -> {
            Ingresso ingressoSalvo = invocation.getArgument(0);
            ingressoSalvo.setId(EVENTO_ID);
            return ingressoSalvo;
        });
        when(eventoService.updateEvento(anyLong(), any(Evento.class))).thenReturn(evento);

        Ingresso resultado = ingressoService.comprarIngresso(EVENTO_ID, PARTICIPANTE_ID);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(EVENTO_ID);
        assertThat(resultado.getEvento()).isEqualTo(evento);
        assertThat(resultado.getEvento().getNome()).isEqualTo("Tech Conference 2026");
        assertThat(resultado.getParticipante()).isEqualTo(participante);
        assertThat(resultado.getParticipante().getNome()).isEqualTo("João Silva");
    }
}

