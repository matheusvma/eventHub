package com.example.eventHub.service;

import com.example.eventHub.domain.Participante;
import com.example.eventHub.repository.ParticipanteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes unitários para ParticipanteService")
class ParticipanteServiceTest {

    private static final Long PARTICIPANTE_ID = 1L;
    private static final Long PARTICIPANTE_ID_INVALIDO = 999L;
    private static final String EMAIL_VALIDO = "joao@example.com";
    private static final String EMAIL_INVALIDO = "invalido@example.com";

    @Mock
    private ParticipanteRepository participanteRepository;

    @InjectMocks
    private ParticipanteService participanteService;

    private Participante participante;

    @BeforeEach
    void setUp() {
        participante = new Participante();
        participante.setId(PARTICIPANTE_ID);
        participante.setNome("João Silva");
        participante.setEmail(EMAIL_VALIDO);
    }

    @Test
    @DisplayName("Deve obter todos os participantes")
    void deveObterTodosOsParticipantes() {
        Participante participante2 = new Participante();
        participante2.setId(2L);
        participante2.setNome("Maria Santos");
        participante2.setEmail("maria@example.com");

        List<Participante> participantesEsperados = Arrays.asList(participante, participante2);
        when(participanteRepository.findAll()).thenReturn(participantesEsperados);

        List<Participante> resultado = participanteService.getAllParticipantes();

        assertThat(resultado).isNotNull().hasSize(2);
        assertThat(resultado).containsExactlyInAnyOrder(participante, participante2);
        verify(participanteRepository).findAll();
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando não há participantes")
    void deveRetornarListaVaziaQuandoNaoHaParticipantes() {
        when(participanteRepository.findAll()).thenReturn(Arrays.asList());

        List<Participante> resultado = participanteService.getAllParticipantes();

        assertThat(resultado).isNotNull().isEmpty();
        verify(participanteRepository).findAll();
    }

    @Test
    @DisplayName("Deve obter participante por ID com sucesso")
    void deveObterParticipantePorIdComSucesso() {
        when(participanteRepository.findById(PARTICIPANTE_ID)).thenReturn(Optional.of(participante));

        Participante resultado = participanteService.getParticipanteById(PARTICIPANTE_ID);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(PARTICIPANTE_ID);
        assertThat(resultado.getNome()).isEqualTo("João Silva");
        verify(participanteRepository).findById(PARTICIPANTE_ID);
    }

    @Test
    @DisplayName("Deve retornar null quando participante não é encontrado por ID")
    void deveRetornarNullQuandoParticipanteNaoEncontradoPorId() {
        when(participanteRepository.findById(PARTICIPANTE_ID_INVALIDO)).thenReturn(Optional.empty());

        Participante resultado = participanteService.getParticipanteById(PARTICIPANTE_ID_INVALIDO);

        assertThat(resultado).isNull();
        verify(participanteRepository).findById(PARTICIPANTE_ID_INVALIDO);
    }

    @Test
    @DisplayName("Deve obter participante por email com sucesso")
    void deveObterParticipantePorEmailComSucesso() {
        when(participanteRepository.findByEmail(EMAIL_VALIDO)).thenReturn(Optional.of(participante));

        Participante resultado = participanteService.getParticipanteByEmail(EMAIL_VALIDO);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getEmail()).isEqualTo(EMAIL_VALIDO);
        assertThat(resultado.getNome()).isEqualTo("João Silva");
        verify(participanteRepository).findByEmail(EMAIL_VALIDO);
    }

    @Test
    @DisplayName("Deve retornar null quando participante não é encontrado por email")
    void deveRetornarNullQuandoParticipanteNaoEncontradoPorEmail() {
        when(participanteRepository.findByEmail(EMAIL_INVALIDO)).thenReturn(Optional.empty());

        Participante resultado = participanteService.getParticipanteByEmail(EMAIL_INVALIDO);

        assertThat(resultado).isNull();
        verify(participanteRepository).findByEmail(EMAIL_INVALIDO);
    }

    @Test
    @DisplayName("Deve criar novo participante com sucesso")
    void deveCriarNovoParticipanteComSucesso() {
        when(participanteRepository.save(any(Participante.class))).thenReturn(participante);

        Participante resultado = participanteService.createParticipante(participante);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(PARTICIPANTE_ID);
        assertThat(resultado.getNome()).isEqualTo("João Silva");
        verify(participanteRepository).save(participante);
    }

    @Test
    @DisplayName("Deve atualizar participante existente com sucesso")
    void deveAtualizarParticipanteExistenteComSucesso() {
        Participante participanteAtualizado = new Participante();
        participanteAtualizado.setNome("João Silva - Updated");
        participanteAtualizado.setEmail("joao.silva@example.com");

        when(participanteRepository.existsById(PARTICIPANTE_ID)).thenReturn(true);
        when(participanteRepository.save(any(Participante.class))).thenReturn(participanteAtualizado);

        Participante resultado = participanteService.updateParticipante(PARTICIPANTE_ID, participanteAtualizado);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getNome()).isEqualTo("João Silva - Updated");
        verify(participanteRepository).existsById(PARTICIPANTE_ID);
        verify(participanteRepository).save(any(Participante.class));
    }

    @Test
    @DisplayName("Deve retornar null ao atualizar participante inexistente")
    void deveRetornarNullAoAtualizarParticipanteInexistente() {
        when(participanteRepository.existsById(PARTICIPANTE_ID_INVALIDO)).thenReturn(false);

        Participante resultado = participanteService.updateParticipante(PARTICIPANTE_ID_INVALIDO, participante);

        assertThat(resultado).isNull();
        verify(participanteRepository).existsById(PARTICIPANTE_ID_INVALIDO);
        verify(participanteRepository, never()).save(any(Participante.class));
    }

    @Test
    @DisplayName("Deve deletar participante existente com sucesso")
    void deveDeletarParticipanteExistenteComSucesso() {
        when(participanteRepository.existsById(PARTICIPANTE_ID)).thenReturn(true);

        boolean resultado = participanteService.deleteParticipante(PARTICIPANTE_ID);

        assertThat(resultado).isTrue();
        verify(participanteRepository).existsById(PARTICIPANTE_ID);
        verify(participanteRepository).deleteById(PARTICIPANTE_ID);
    }

    @Test
    @DisplayName("Deve retornar false ao deletar participante inexistente")
    void deveRetornarFalseAoDeletarParticipanteInexistente() {
        when(participanteRepository.existsById(PARTICIPANTE_ID_INVALIDO)).thenReturn(false);

        boolean resultado = participanteService.deleteParticipante(PARTICIPANTE_ID_INVALIDO);

        assertThat(resultado).isFalse();
        verify(participanteRepository).existsById(PARTICIPANTE_ID_INVALIDO);
        verify(participanteRepository, never()).deleteById(anyLong());
    }

    @Test
    @DisplayName("Deve preservar ID ao atualizar participante")
    void devePreservarIdAoAtualizarParticipante() {
        Participante participanteAtualizado = new Participante();
        participanteAtualizado.setNome("João Silva - Updated");
        participanteAtualizado.setEmail("joao.silva@example.com");

        when(participanteRepository.existsById(PARTICIPANTE_ID)).thenReturn(true);
        when(participanteRepository.save(any(Participante.class))).thenAnswer(invocation -> {
            Participante p = invocation.getArgument(0);
            p.setId(PARTICIPANTE_ID);
            return p;
        });

        Participante resultado = participanteService.updateParticipante(PARTICIPANTE_ID, participanteAtualizado);

        assertThat(resultado.getId()).isEqualTo(PARTICIPANTE_ID);
    }

    @Test
    @DisplayName("Deve validar email único ao criar participante")
    void deveValidarEmailUnicoAoCriarParticipante() {
        when(participanteRepository.save(any(Participante.class))).thenReturn(participante);

        Participante resultado = participanteService.createParticipante(participante);

        assertThat(resultado.getEmail()).isEqualTo(EMAIL_VALIDO);
        verify(participanteRepository).save(participante);
    }
}

