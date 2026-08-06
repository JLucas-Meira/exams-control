package br.com.joaolucas.exams_control.dto.request;

import br.com.joaolucas.exams_control.model.enums.Status;

import java.time.LocalDate;

public record ExamRequestPatchDTO(
        Status status,
        LocalDate scheduledDate
) {
}
