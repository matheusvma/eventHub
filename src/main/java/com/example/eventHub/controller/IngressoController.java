package com.example.eventHub.controller;

import com.example.eventHub.domain.Ingresso;
import com.example.eventHub.dto.IngressoResponse;
import com.example.eventHub.mapper.IngressoMapper;
import com.example.eventHub.service.IngressoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ingressos")
@Tag(name = "Ingressos", description = "API para gerenciar compra de ingressos em eventos")
public class IngressoController {

    private final IngressoService ingressoService;
    private final IngressoMapper ingressoMapper;

    public IngressoController(IngressoService ingressoService, IngressoMapper ingressoMapper) {
        this.ingressoService = ingressoService;
        this.ingressoMapper = ingressoMapper;
    }

    @PostMapping("/eventos/{eventId}/participantes/{participantId}")
    @Operation(summary = "Comprar ingresso em evento", description = "Compra um ingresso em um evento")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Compra feita com sucesso"),
            @ApiResponse(responseCode = "404", description = "Evento ou participante não encontrado"),
            @ApiResponse(responseCode = "409", description = "Evento lotado ou participante já inscrito")
    })
    public ResponseEntity<IngressoResponse> comprarIngresso(
            @Parameter(description = "ID do evento", required = true) @PathVariable Long eventId,
            @Parameter(description = "ID do participante", required = true) @PathVariable Long participantId) {

        Ingresso ingresso = ingressoService.comprarIngresso(eventId, participantId);
        IngressoResponse response = ingressoMapper.toResponse(ingresso);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/eventos/{eventId}")
    @Operation(summary = "Listar ingressos do evento", description = "Lista todos os ingressos comprados para um evento específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ingressos listados com sucesso")
    })
    public ResponseEntity<List<IngressoResponse>> listarIngressosDoEvento(
            @Parameter(description = "ID do evento", required = true) @PathVariable Long eventId) {

        List<Ingresso> ingressos = ingressoService.listarIngressosPorEvento(eventId);
        List<IngressoResponse> response = ingressoMapper.toResponseList(ingressos);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/participantes/{participantId}")
    @Operation(summary = "Listar ingressos de um participante", description = "Lista todos os ingressos comprados por um participante")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ingressos listados com sucesso")
    })
    public ResponseEntity<List<IngressoResponse>> listarIngressosPorParticipante(
            @Parameter(description = "ID do participante", required = true) @PathVariable Long participantId) {

        List<Ingresso> ingressos = ingressoService.listarIngressosPorParticipante(participantId);
        List<IngressoResponse> response = ingressoMapper.toResponseList(ingressos);

        return ResponseEntity.ok(response);
    }
}

