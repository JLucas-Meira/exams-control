package br.com.joaolucas.exams_control.dto.response;

import java.util.UUID;

public record ProcedureResponseDTO(
        UUID id,
        String name,
        String description
) {
}
