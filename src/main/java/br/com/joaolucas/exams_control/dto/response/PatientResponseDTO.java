package br.com.joaolucas.exams_control.dto.response;

import br.com.joaolucas.exams_control.model.entities.Patient;

import java.time.LocalDate;
import java.util.UUID;

public record PatientResponseDTO(
        UUID id,
        String name,
        LocalDate birthDate,
        String susCard,
        String phoneNumber
){
    public static PatientResponseDTO fromEntity(Patient patient) {
        return new PatientResponseDTO(
                patient.getId(),
                patient.getName(),
                patient.getBirthDate(),
                patient.getSusCard(),
                patient.getPhoneNumber()
        );
    }
}
