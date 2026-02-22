package com.example.eventHub.mapper;

import com.example.eventHub.domain.Ingresso;
import com.example.eventHub.dto.IngressoResponse;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class IngressoMapper {

    public IngressoResponse toResponse(Ingresso ingresso) {
        if (ingresso == null) {
            return null;
        }

        IngressoResponse.EventoDTO eventoDTO = new IngressoResponse.EventoDTO(
                ingresso.getEvento().getId(),
                ingresso.getEvento().getNome(),
                ingresso.getEvento().getData(),
                ingresso.getEvento().getLocal(),
                ingresso.getEvento().getCapacidade()
        );

        IngressoResponse.ParticipanteDTO participanteDTO = new IngressoResponse.ParticipanteDTO(
                ingresso.getParticipante().getId(),
                ingresso.getParticipante().getNome(),
                ingresso.getParticipante().getEmail()
        );

        return new IngressoResponse(
                ingresso.getId(),
                eventoDTO,
                participanteDTO
        );
    }

    public List<IngressoResponse> toResponseList(List<Ingresso> ingressos) {
        if (ingressos == null) {
            return List.of();
        }

        return ingressos.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
}

