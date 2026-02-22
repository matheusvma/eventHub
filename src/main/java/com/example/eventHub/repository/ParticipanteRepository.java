package com.example.eventHub.repository;

import com.example.eventHub.domain.Participante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ParticipanteRepository extends JpaRepository<Participante, Long> {

    /**
     * Busca um participante pelo email
     * @param email email do participante
     * @return Optional contendo o participante se encontrado
     */
    Optional<Participante> findByEmail(String email);
}

