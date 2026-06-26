package br.com.joaolucas.exams_control.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record PatientCreateDTO(

        @NotBlank(message = "Digite o nome do paciente")
        String name,

        @NotNull(message = "Digite uma data de nascimento valida")
        @Past
        LocalDate birthDate,

        @NotBlank(message = "Digite o número do cartão do SUS")
        @Size(min = 15, max = 15)
        String susCard,

        @NotBlank(message = "Digite o número de telefone")
        @Size(min = 10, max = 11)
        String phoneNumber
){}
