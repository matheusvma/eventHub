package com.example.eventHub.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Trata exceções de negócio customizadas
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(
            BusinessException ex,
            WebRequest request) {

        ErrorResponse errorResponse = new ErrorResponse(
                ex.getHttpStatus(),
                ex.getCode(),
                ex.getMessage(),
                request.getDescription(false).replace("uri=", "")
        );

        return ResponseEntity
                .status(ex.getHttpStatus())
                .body(errorResponse);
    }

    /**
     * Trata exceções específicas de evento não encontrado
     */
    @ExceptionHandler(EventoNaoEncontradoException.class)
    public ResponseEntity<ErrorResponse> handleEventoNaoEncontradoException(
            EventoNaoEncontradoException ex,
            WebRequest request) {

        ErrorResponse errorResponse = new ErrorResponse(
                ex.getHttpStatus(),
                ex.getCode(),
                ex.getMessage(),
                request.getDescription(false).replace("uri=", "")
        );

        return ResponseEntity
                .status(ex.getHttpStatus())
                .body(errorResponse);
    }

    /**
     * Trata exceções de evento lotado
     */
    @ExceptionHandler(EventoLotadoException.class)
    public ResponseEntity<ErrorResponse> handleEventoLotadoException(
            EventoLotadoException ex,
            WebRequest request) {

        ErrorResponse errorResponse = new ErrorResponse(
                ex.getHttpStatus(),
                ex.getCode(),
                ex.getMessage(),
                request.getDescription(false).replace("uri=", "")
        );

        return ResponseEntity
                .status(ex.getHttpStatus())
                .body(errorResponse);
    }

    /**
     * Trata exceções de participante não encontrado
     */
    @ExceptionHandler(ParticipanteNaoEncontradoException.class)
    public ResponseEntity<ErrorResponse> handleParticipanteNaoEncontradoException(
            ParticipanteNaoEncontradoException ex,
            WebRequest request) {

        ErrorResponse errorResponse = new ErrorResponse(
                ex.getHttpStatus(),
                ex.getCode(),
                ex.getMessage(),
                request.getDescription(false).replace("uri=", "")
        );

        return ResponseEntity
                .status(ex.getHttpStatus())
                .body(errorResponse);
    }

    /**
     * Trata exceções de participante já inscrito
     */
    @ExceptionHandler(ParticipanteJaInscritoException.class)
    public ResponseEntity<ErrorResponse> handleParticipanteJaInscritoException(
            ParticipanteJaInscritoException ex,
            WebRequest request) {

        ErrorResponse errorResponse = new ErrorResponse(
                ex.getHttpStatus(),
                ex.getCode(),
                ex.getMessage(),
                request.getDescription(false).replace("uri=", "")
        );

        return ResponseEntity
                .status(ex.getHttpStatus())
                .body(errorResponse);
    }

    /**
     * Trata exceções de validação
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {

        Map<String, Object> response = new HashMap<>();
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("error", "Erro de validação");
        response.put("errors", errors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /**
     * Trata exceções genéricas não previstas
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(
            Exception ex,
            WebRequest request) {

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "ERRO_INTERNO",
                "Ocorreu um erro interno no servidor",
                request.getDescription(false).replace("uri=", "")
        );

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(errorResponse);
    }
}

