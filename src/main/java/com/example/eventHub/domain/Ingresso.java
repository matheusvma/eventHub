package com.example.eventHub.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "event_participants", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"event_id", "participant_id"})
})
@Schema(description = "Entidade que representa a ingresso de um participante em um evento")
@AllArgsConstructor
@NoArgsConstructor
public class Ingresso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único da inscrição", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @NotNull(message = "O evento é obrigatório")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "event_id", nullable = false)
    @Schema(description = "Evento ao qual o participante está inscrito")
    private Evento evento;

    @NotNull(message = "O participante é obrigatório")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "participant_id", nullable = false)
    @Schema(description = "Participante inscrito no evento")
    private Participante participante;

    @Column(nullable = false)
    @Schema(description = "Data e hora da inscrição", example = "2026-02-22T13:25:09", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime registrationDate;

    @PrePersist
    protected void onCreate() {
        registrationDate = LocalDateTime.now();
    }
}

