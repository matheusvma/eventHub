package com.example.eventHub.controller;

import com.example.eventHub.domain.Participante;
import com.example.eventHub.service.ParticipanteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/participantes")
@Tag(name = "Participantes", description = "API para gerenciamento de participantes")
public class ParticipanteController {

    @Autowired
    private ParticipanteService participanteService;

    @GetMapping
    @Operation(summary = "Listar todos os participantes", description = "Retorna uma lista com todos os participantes cadastrados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de participantes retornada com sucesso")
    })
    public ResponseEntity<List<Participante>> getAllParticipantes() {
        List<Participante> participantes = participanteService.getAllParticipantes();
        return ResponseEntity.ok(participantes);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar participante por ID", description = "Retorna um participante específico através do seu ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Participante encontrado"),
            @ApiResponse(responseCode = "404", description = "Participante não encontrado")
    })
    public ResponseEntity<Participante> getParticipanteById(
            @Parameter(description = "ID do participante", required = true) @PathVariable Long id) {
        Participante participante = participanteService.getParticipanteById(id);
        if (participante != null) {
            return ResponseEntity.ok(participante);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    @Operation(summary = "Criar participante", description = "Cria um novo participante")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Participante criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    public ResponseEntity<Participante> createParticipante(
            @Parameter(description = "Dados do participante a ser criado", required = true)
            @Valid @RequestBody Participante participante) {
        Participante createdParticipante = participanteService.createParticipante(participante);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdParticipante);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar participante", description = "Atualiza os dados de um participante existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Participante atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Participante não encontrado")
    })
    public ResponseEntity<Participante> updateParticipante(
            @Parameter(description = "ID do participante", required = true) @PathVariable Long id,
            @Parameter(description = "Dados atualizados do participante", required = true)
            @Valid @RequestBody Participante participante) {
        Participante updatedParticipante = participanteService.updateParticipante(id, participante);
        if (updatedParticipante != null) {
            return ResponseEntity.ok(updatedParticipante);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar participante", description = "Remove um participante do sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Participante deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Participante não encontrado")
    })
    public ResponseEntity<Void> deleteParticipante(
            @Parameter(description = "ID do participante", required = true) @PathVariable Long id) {
        boolean deleted = participanteService.deleteParticipante(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}

