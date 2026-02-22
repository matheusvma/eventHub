package com.example.eventHub.validator;

import com.example.eventHub.domain.Evento;
import com.example.eventHub.domain.Participante;
import com.example.eventHub.exception.EventoLotadoException;
import com.example.eventHub.exception.EventoNaoEncontradoException;
import com.example.eventHub.exception.ParticipanteJaInscritoException;
import com.example.eventHub.exception.ParticipanteNaoEncontradoException;
import com.example.eventHub.repository.IngressoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes unitários para IngressoValidator - Validação de Capacidade")
class IngressoValidatorTest {

    private static final Long EVENTO_ID = 1L;
    private static final Long PARTICIPANTE_ID = 1L;
    private static final Long EVENTO_ID_GRANDE = 999L;
    private static final Long PARTICIPANTE_ID_ALTERNATIVO = 888L;
    private static final int CAPACIDADE_PADRAO = 100;
    private static final int CAPACIDADE_PEQUENA = 1;
    private static final int CAPACIDADE_GRANDE = 10000;

    @Mock
    private IngressoRepository ingressoRepository;

    @InjectMocks
    private IngressoValidator ingressoValidator;

    private Evento evento;
    private Participante participante;

    @BeforeEach
    void setUp() {
        evento = new Evento();
        evento.setId(EVENTO_ID);
        evento.setNome("Tech Conference 2026");
        evento.setData(LocalDateTime.of(2026, 3, 15, 9, 0));
        evento.setLocal("São Paulo");
        evento.setCapacidade(CAPACIDADE_PADRAO);

        participante = new Participante();
        participante.setId(PARTICIPANTE_ID);
        participante.setNome("João Silva");
        participante.setEmail("joao@example.com");
    }

    @Test
    @DisplayName("Deve validar com sucesso quando todas as condições são atendidas")
    void deveValidarComSucessoQuandoTodasCondicoesAtendidas() {
        when(ingressoRepository.existsByEventoIdAndParticipanteId(EVENTO_ID, PARTICIPANTE_ID)).thenReturn(false);
        when(ingressoRepository.countByEventoId(EVENTO_ID)).thenReturn(50L);

        assertThatCode(() -> ingressoValidator.validaCompraIngresso(evento, participante, EVENTO_ID, PARTICIPANTE_ID))
                .doesNotThrowAnyException();

        verify(ingressoRepository).existsByEventoIdAndParticipanteId(EVENTO_ID, PARTICIPANTE_ID);
        verify(ingressoRepository).countByEventoId(EVENTO_ID);
    }

    @Test
    @DisplayName("Deve lançar exceção quando evento é null")
    void deveLancarExcecaoQuandoEventoNull() {
        assertThatThrownBy(() -> ingressoValidator.validaCompraIngresso(null, participante, EVENTO_ID, PARTICIPANTE_ID))
                .isInstanceOf(EventoNaoEncontradoException.class)
                .hasMessageContaining("Evento com ID 1 não foi encontrado");

        verify(ingressoRepository, never()).existsByEventoIdAndParticipanteId(anyLong(), anyLong());
        verify(ingressoRepository, never()).countByEventoId(anyLong());
    }

    @Test
    @DisplayName("Deve lançar exceção quando participante é null")
    void deveLancarExcecaoQuandoParticipanteNull() {
        assertThatThrownBy(() -> ingressoValidator.validaCompraIngresso(evento, null, EVENTO_ID, PARTICIPANTE_ID))
                .isInstanceOf(ParticipanteNaoEncontradoException.class)
                .hasMessageContaining("Participante com ID 1 não foi encontrado");

        verify(ingressoRepository, never()).existsByEventoIdAndParticipanteId(anyLong(), anyLong());
        verify(ingressoRepository, never()).countByEventoId(anyLong());
    }

    @Test
    @DisplayName("Deve lançar exceção quando participante já está inscrito")
    void deveLancarExcecaoQuandoParticipanteJaInscrito() {
        when(ingressoRepository.existsByEventoIdAndParticipanteId(EVENTO_ID, PARTICIPANTE_ID)).thenReturn(true);

        assertThatThrownBy(() -> ingressoValidator.validaCompraIngresso(evento, participante, EVENTO_ID, PARTICIPANTE_ID))
                .isInstanceOf(ParticipanteJaInscritoException.class)
                .hasMessageContaining("Participante com ID 1 já está inscrito no evento com ID 1");

        verify(ingressoRepository).existsByEventoIdAndParticipanteId(EVENTO_ID, PARTICIPANTE_ID);
        verify(ingressoRepository, never()).countByEventoId(anyLong());
    }

    @Test
    @DisplayName("Deve lançar exceção quando evento está exatamente na capacidade máxima")
    void deveLancarExcecaoQuandoEventoNaCapacidadeMaxima() {
        evento.setCapacidade(CAPACIDADE_PADRAO);
        when(ingressoRepository.existsByEventoIdAndParticipanteId(EVENTO_ID, PARTICIPANTE_ID)).thenReturn(false);
        when(ingressoRepository.countByEventoId(EVENTO_ID)).thenReturn(100L);

        assertThatThrownBy(() -> ingressoValidator.validaCompraIngresso(evento, participante, EVENTO_ID, PARTICIPANTE_ID))
                .isInstanceOf(EventoLotadoException.class)
                .hasMessageContaining("Evento com ID 1 está lotado")
                .hasMessageContaining("Capacidade máxima: 100");

        verify(ingressoRepository).existsByEventoIdAndParticipanteId(EVENTO_ID, PARTICIPANTE_ID);
        verify(ingressoRepository).countByEventoId(EVENTO_ID);
    }

    @Test
    @DisplayName("Deve lançar exceção quando evento está acima da capacidade")
    void deveLancarExcecaoQuandoEventoAcimaDaCapacidade() {
        evento.setCapacidade(50);
        when(ingressoRepository.existsByEventoIdAndParticipanteId(EVENTO_ID, PARTICIPANTE_ID)).thenReturn(false);
        when(ingressoRepository.countByEventoId(EVENTO_ID)).thenReturn(75L);

        assertThatThrownBy(() -> ingressoValidator.validaCompraIngresso(evento, participante, EVENTO_ID, PARTICIPANTE_ID))
                .isInstanceOf(EventoLotadoException.class)
                .hasMessageContaining("Evento com ID 1 está lotado")
                .hasMessageContaining("Capacidade máxima: 50");

        verify(ingressoRepository).existsByEventoIdAndParticipanteId(EVENTO_ID, PARTICIPANTE_ID);
        verify(ingressoRepository).countByEventoId(EVENTO_ID);
    }

    @Test
    @DisplayName("Deve validar com sucesso quando há exatamente 1 vaga disponível")
    void deveValidarComSucessoQuandoHaUmaVagaDisponivel() {
        evento.setCapacidade(CAPACIDADE_PADRAO);
        when(ingressoRepository.existsByEventoIdAndParticipanteId(EVENTO_ID, PARTICIPANTE_ID)).thenReturn(false);
        when(ingressoRepository.countByEventoId(EVENTO_ID)).thenReturn(99L);

        assertThatCode(() -> ingressoValidator.validaCompraIngresso(evento, participante, EVENTO_ID, PARTICIPANTE_ID))
                .doesNotThrowAnyException();

        verify(ingressoRepository).existsByEventoIdAndParticipanteId(EVENTO_ID, PARTICIPANTE_ID);
        verify(ingressoRepository).countByEventoId(EVENTO_ID);
    }

    @Test
    @DisplayName("Deve validar com sucesso quando evento está vazio")
    void deveValidarComSucessoQuandoEventoVazio() {
        when(ingressoRepository.existsByEventoIdAndParticipanteId(EVENTO_ID, PARTICIPANTE_ID)).thenReturn(false);
        when(ingressoRepository.countByEventoId(EVENTO_ID)).thenReturn(0L);

        assertThatCode(() -> ingressoValidator.validaCompraIngresso(evento, participante, EVENTO_ID, PARTICIPANTE_ID))
                .doesNotThrowAnyException();

        verify(ingressoRepository).existsByEventoIdAndParticipanteId(EVENTO_ID, PARTICIPANTE_ID);
        verify(ingressoRepository).countByEventoId(EVENTO_ID);
    }

    @Test
    @DisplayName("Deve lançar exceção quando evento tem capacidade 1 e já tem 1 inscrito")
    void deveLancarExcecaoEventoCapacidade1Lotado() {
        evento.setCapacidade(CAPACIDADE_PEQUENA);
        when(ingressoRepository.existsByEventoIdAndParticipanteId(EVENTO_ID, PARTICIPANTE_ID)).thenReturn(false);
        when(ingressoRepository.countByEventoId(EVENTO_ID)).thenReturn(1L);

        assertThatThrownBy(() -> ingressoValidator.validaCompraIngresso(evento, participante, EVENTO_ID, PARTICIPANTE_ID))
                .isInstanceOf(EventoLotadoException.class)
                .hasMessageContaining("Evento com ID 1 está lotado")
                .hasMessageContaining("Capacidade máxima: 1");

        verify(ingressoRepository).existsByEventoIdAndParticipanteId(EVENTO_ID, PARTICIPANTE_ID);
        verify(ingressoRepository).countByEventoId(EVENTO_ID);
    }

    @Test
    @DisplayName("Deve validar quando evento tem capacidade 1 e está vazio")
    void deveValidarEventoCapacidade1Vazio() {
        evento.setCapacidade(CAPACIDADE_PEQUENA);
        when(ingressoRepository.existsByEventoIdAndParticipanteId(EVENTO_ID, PARTICIPANTE_ID)).thenReturn(false);
        when(ingressoRepository.countByEventoId(EVENTO_ID)).thenReturn(0L);

        assertThatCode(() -> ingressoValidator.validaCompraIngresso(evento, participante, EVENTO_ID, PARTICIPANTE_ID))
                .doesNotThrowAnyException();

        verify(ingressoRepository).existsByEventoIdAndParticipanteId(EVENTO_ID, PARTICIPANTE_ID);
        verify(ingressoRepository).countByEventoId(EVENTO_ID);
    }

    @Test
    @DisplayName("Deve verificar ordem de validações: evento -> participante -> inscrição -> capacidade")
    void deveVerificarOrdemDeValidacoes() {
        when(ingressoRepository.existsByEventoIdAndParticipanteId(EVENTO_ID, PARTICIPANTE_ID)).thenReturn(false);
        when(ingressoRepository.countByEventoId(EVENTO_ID)).thenReturn(50L);

        ingressoValidator.validaCompraIngresso(evento, participante, EVENTO_ID, PARTICIPANTE_ID);

        var inOrder = inOrder(ingressoRepository);
        inOrder.verify(ingressoRepository).existsByEventoIdAndParticipanteId(EVENTO_ID, PARTICIPANTE_ID);
        inOrder.verify(ingressoRepository).countByEventoId(EVENTO_ID);
    }

    @Test
    @DisplayName("Deve validar com diferentes IDs de evento e participante")
    void deveValidarComDiferentesIds() {
        evento.setId(EVENTO_ID_GRANDE);
        participante.setId(PARTICIPANTE_ID_ALTERNATIVO);

        when(ingressoRepository.existsByEventoIdAndParticipanteId(EVENTO_ID_GRANDE, PARTICIPANTE_ID_ALTERNATIVO)).thenReturn(false);
        when(ingressoRepository.countByEventoId(EVENTO_ID_GRANDE)).thenReturn(10L);

        assertThatCode(() -> ingressoValidator.validaCompraIngresso(evento, participante, EVENTO_ID_GRANDE, PARTICIPANTE_ID_ALTERNATIVO))
                .doesNotThrowAnyException();

        verify(ingressoRepository).existsByEventoIdAndParticipanteId(EVENTO_ID_GRANDE, PARTICIPANTE_ID_ALTERNATIVO);
        verify(ingressoRepository).countByEventoId(EVENTO_ID_GRANDE);
    }

    @Test
    @DisplayName("Deve lançar exceção com mensagem correta quando capacidade é grande")
    void deveLancarExcecaoComMensagemCorretaCapacidadeGrande() {
        evento.setCapacidade(CAPACIDADE_GRANDE);
        when(ingressoRepository.existsByEventoIdAndParticipanteId(EVENTO_ID, PARTICIPANTE_ID)).thenReturn(false);
        when(ingressoRepository.countByEventoId(EVENTO_ID)).thenReturn(10000L);

        assertThatThrownBy(() -> ingressoValidator.validaCompraIngresso(evento, participante, EVENTO_ID, PARTICIPANTE_ID))
                .isInstanceOf(EventoLotadoException.class)
                .hasMessageContaining("Capacidade máxima: 10000");
    }

    @Test
    @DisplayName("Não deve chamar countByEventoId se participante já está inscrito")
    void naoDeveChamarCountSeParticipanteJaInscrito() {
        when(ingressoRepository.existsByEventoIdAndParticipanteId(EVENTO_ID, PARTICIPANTE_ID)).thenReturn(true);

        assertThatThrownBy(() -> ingressoValidator.validaCompraIngresso(evento, participante, EVENTO_ID, PARTICIPANTE_ID))
                .isInstanceOf(ParticipanteJaInscritoException.class);

        verify(ingressoRepository).existsByEventoIdAndParticipanteId(EVENTO_ID, PARTICIPANTE_ID);
        verify(ingressoRepository, never()).countByEventoId(anyLong());
    }
}

