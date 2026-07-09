package br.com.joaolucas.exams_control.dto.response;

import br.com.joaolucas.exams_control.model.entities.Procedure;

import java.util.UUID;

public record ProcedureResponseDTO(
        UUID id,
        String name,
        String description,
        Integer montlyQuota
) {
    public static ProcedureResponseDTO fromEntity (Procedure procedure){
        return new ProcedureResponseDTO(
                procedure.getId(),
                procedure.getName(),
                procedure.getDescription(),
                procedure.getMontlyQuota()
        );
    }
}
