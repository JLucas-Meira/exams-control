package br.com.joaolucas.exams_control.dto.request;

import java.time.LocalDate;

public record PatientPatchDTO(
        String name,
        LocalDate birthDate,
        String susCard,
        String phoneNumber
) {
}
