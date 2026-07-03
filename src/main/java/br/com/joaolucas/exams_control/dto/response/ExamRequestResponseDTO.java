package br.com.joaolucas.exams_control.dto.response;

import br.com.joaolucas.exams_control.model.entities.ExamRequest;
import br.com.joaolucas.exams_control.model.enums.Priority;
import br.com.joaolucas.exams_control.model.enums.Status;

import java.time.LocalDate;
import java.util.UUID;

public record ExamRequestResponseDTO(
        UUID id,
        String patientName,
        String procedureName,
        Priority priority,
        Status status,
        LocalDate scheduledDate,
        LocalDate createdAt
) {
    public static ExamRequestResponseDTO fromEntity(ExamRequest examRequest){
        return new ExamRequestResponseDTO(
                examRequest.getId(),
                examRequest.getPatient().getName(),
                examRequest.getProcedure().getName(),
                examRequest.getPriority(),
                examRequest.getStatus(),
                examRequest.getScheduledDate(),
                examRequest.getCreatedAt()
        );
    }
}
