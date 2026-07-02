package br.com.joaolucas.exams_control.dto.response;

import java.time.LocalDate;
import java.util.UUID;

public record PatientResponseDTO(
        UUID id,
        String name,
        LocalDate birthDate,
        String susCard,
        String phoneNumber
) {
}
