package com.example.eventHub.service;

import com.example.eventHub.domain.Evento;
import com.example.eventHub.repository.EventoRepository;
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
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes unitários para EventoService")
class EventoServiceTest {

    private static final Long EVENTO_ID = 1L;
    private static final Long EVENTO_ID_INVALIDO = 999L;

    @Mock
    private EventoRepository eventoRepository;

    @InjectMocks
    private EventoService eventoService;

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
    @DisplayName("Deve obter todos os eventos")
    void deveObterTodosOsEventos() {
        Evento evento2 = new Evento();
        evento2.setId(2L);
        evento2.setNome("Web Summit 2026");

        List<Evento> eventosEsperados = Arrays.asList(evento, evento2);
        when(eventoRepository.findAll()).thenReturn(eventosEsperados);

        List<Evento> resultado = eventoService.getAllEventos();

        assertThat(resultado).isNotNull().hasSize(2);
        assertThat(resultado).containsExactlyInAnyOrder(evento, evento2);
        verify(eventoRepository).findAll();
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando não há eventos")
    void deveRetornarListaVaziaQuandoNaoHaEventos() {
        when(eventoRepository.findAll()).thenReturn(Arrays.asList());

        List<Evento> resultado = eventoService.getAllEventos();

        assertThat(resultado).isNotNull().isEmpty();
        verify(eventoRepository).findAll();
    }

    @Test
    @DisplayName("Deve obter evento por ID com sucesso")
    void deveObterEventoPorIdComSucesso() {
        when(eventoRepository.findById(EVENTO_ID)).thenReturn(Optional.of(evento));

        Evento resultado = eventoService.getEventoById(EVENTO_ID);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(EVENTO_ID);
        assertThat(resultado.getNome()).isEqualTo("Tech Conference 2026");
        verify(eventoRepository).findById(EVENTO_ID);
    }

    @Test
    @DisplayName("Deve retornar null quando evento não é encontrado")
    void deveRetornarNullQuandoEventoNaoEncontrado() {
        when(eventoRepository.findById(EVENTO_ID_INVALIDO)).thenReturn(Optional.empty());

        Evento resultado = eventoService.getEventoById(EVENTO_ID_INVALIDO);

        assertThat(resultado).isNull();
        verify(eventoRepository).findById(EVENTO_ID_INVALIDO);
    }

    @Test
    @DisplayName("Deve criar novo evento com sucesso")
    void deveCriarNovoEventoComSucesso() {
        when(eventoRepository.save(any(Evento.class))).thenReturn(evento);

        Evento resultado = eventoService.createEvento(evento);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(EVENTO_ID);
        assertThat(resultado.getNome()).isEqualTo("Tech Conference 2026");
        verify(eventoRepository).save(evento);
    }

    @Test
    @DisplayName("Deve atualizar evento existente com sucesso")
    void deveAtualizarEventoExistenteComSucesso() {
        Evento eventoAtualizado = new Evento();
        eventoAtualizado.setNome("Tech Conference 2026 - Updated");
        eventoAtualizado.setData(LocalDateTime.of(2026, 4, 15, 9, 0));
        eventoAtualizado.setLocal("Rio de Janeiro");
        eventoAtualizado.setCapacidade(150);

        when(eventoRepository.existsById(EVENTO_ID)).thenReturn(true);
        when(eventoRepository.save(any(Evento.class))).thenReturn(eventoAtualizado);

        Evento resultado = eventoService.updateEvento(EVENTO_ID, eventoAtualizado);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getNome()).isEqualTo("Tech Conference 2026 - Updated");
        assertThat(resultado.getCapacidade()).isEqualTo(150);
        verify(eventoRepository).existsById(EVENTO_ID);
        verify(eventoRepository).save(any(Evento.class));
    }

    @Test
    @DisplayName("Deve retornar null ao atualizar evento inexistente")
    void deveRetornarNullAoAtualizarEventoInexistente() {
        when(eventoRepository.existsById(EVENTO_ID_INVALIDO)).thenReturn(false);

        Evento resultado = eventoService.updateEvento(EVENTO_ID_INVALIDO, evento);

        assertThat(resultado).isNull();
        verify(eventoRepository).existsById(EVENTO_ID_INVALIDO);
        verify(eventoRepository, never()).save(any(Evento.class));
    }

    @Test
    @DisplayName("Deve deletar evento existente com sucesso")
    void deveDeletarEventoExistenteComSucesso() {
        when(eventoRepository.existsById(EVENTO_ID)).thenReturn(true);

        boolean resultado = eventoService.deleteEvento(EVENTO_ID);

        assertThat(resultado).isTrue();
        verify(eventoRepository).existsById(EVENTO_ID);
        verify(eventoRepository).deleteById(EVENTO_ID);
    }

    @Test
    @DisplayName("Deve retornar false ao deletar evento inexistente")
    void deveRetornarFalseAoDeletarEventoInexistente() {
        when(eventoRepository.existsById(EVENTO_ID_INVALIDO)).thenReturn(false);

        boolean resultado = eventoService.deleteEvento(EVENTO_ID_INVALIDO);

        assertThat(resultado).isFalse();
        verify(eventoRepository).existsById(EVENTO_ID_INVALIDO);
        verify(eventoRepository, never()).deleteById(anyLong());
    }

    @Test
    @DisplayName("Deve validar dados do evento ao criar")
    void deveValidarDadosDoEventoAoCriar() {
        Evento eventoInvalido = new Evento();
        eventoInvalido.setNome("Evento Teste");

        when(eventoRepository.save(any(Evento.class))).thenReturn(eventoInvalido);

        Evento resultado = eventoService.createEvento(eventoInvalido);

        assertThat(resultado).isNotNull();
        verify(eventoRepository).save(eventoInvalido);
    }

    @Test
    @DisplayName("Deve preservar ID ao atualizar evento")
    void devePreservarIdAoAtualizarEvento() {
        Evento eventoAtualizado = new Evento();
        eventoAtualizado.setNome("Tech Conference 2026 - Updated");

        when(eventoRepository.existsById(EVENTO_ID)).thenReturn(true);
        when(eventoRepository.save(any(Evento.class))).thenAnswer(invocation -> {
            Evento evt = invocation.getArgument(0);
            evt.setId(EVENTO_ID);
            return evt;
        });

        Evento resultado = eventoService.updateEvento(EVENTO_ID, eventoAtualizado);

        assertThat(resultado.getId()).isEqualTo(EVENTO_ID);
    }
}

