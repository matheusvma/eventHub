package com.example.eventHub.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Schema(description = "DTO para requisição de compra de ingresso")
public class CompraIngressoRequest {

    @NotNull(message = "O ID do evento é obrigatório")
    @Positive(message = "O ID do evento deve ser positivo")
    @Schema(description = "ID do evento", example = "1", required = true)
    private Long eventoId;

    @NotNull(message = "O ID do participante é obrigatório")
    @Positive(message = "O ID do participante deve ser positivo")
    @Schema(description = "ID do participante", example = "1", required = true)
    private Long participanteId;

    public CompraIngressoRequest() {
    }

    public CompraIngressoRequest(Long eventoId, Long participanteId) {
        this.eventoId = eventoId;
        this.participanteId = participanteId;
    }

    public Long getEventoId() {
        return eventoId;
    }

    public void setEventoId(Long eventoId) {
        this.eventoId = eventoId;
    }

    public Long getParticipanteId() {
        return participanteId;
    }

    public void setParticipanteId(Long participanteId) {
        this.participanteId = participanteId;
    }
}

