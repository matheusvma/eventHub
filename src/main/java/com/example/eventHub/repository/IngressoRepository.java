package com.example.eventHub.repository;

import com.example.eventHub.domain.Ingresso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IngressoRepository extends JpaRepository<Ingresso, Long> {

    /**
     * Busca todas as inscrições de um evento
     * @param eventoId ID do evento
     * @return Lista de inscrições
     */
    List<Ingresso> findByEventoId(Long eventoId);

    /**
     * Busca todas as inscrições de um participante
     * @param participanteId ID do participante
     * @return Lista de inscrições
     */
    List<Ingresso> findByParticipanteId(Long participanteId);

    /**
     * Busca uma inscrição específica
     * @param eventoId ID do evento
     * @param participanteId ID do participante
     * @return Optional contendo a inscrição se encontrada
     */
    Optional<Ingresso> findByEventoIdAndParticipanteId(Long eventoId, Long participanteId);

    /**
     * Verifica se um participante já está inscrito em um evento
     * @param eventoId ID do evento
     * @param participanteId ID do participante
     * @return true se está inscrito, false caso contrário
     */
    boolean existsByEventoIdAndParticipanteId(Long eventoId, Long participanteId);

    /**
     * Conta o número de participantes inscritos em um evento
     * @param eventoId ID do evento
     * @return Número de participantes
     */
    @Query("SELECT COUNT(ep) FROM Ingresso ep WHERE ep.evento.id = :eventoId")
    long countByEventoId(@Param("eventoId") Long eventoId);

    /**
     * Busca todos os eventos nos quais um participante está inscrito
     * @param participanteId ID do participante
     * @return Lista de inscrições com eventos
     */
    @Query("SELECT ep FROM Ingresso ep WHERE ep.participante.id = :participanteId")
    List<Ingresso> findAllEventosByParticipanteId(@Param("participanteId") Long participanteId);
}

