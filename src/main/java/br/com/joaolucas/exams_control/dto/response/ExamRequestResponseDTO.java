package br.com.joaolucas.exams_control.dto.response;

import br.com.joaolucas.exams_control.model.entities.Patient;
import br.com.joaolucas.exams_control.model.entities.Procedure;
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
}
