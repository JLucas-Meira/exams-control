package br.com.joaolucas.exams_control.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProcedureCreateDTO(

        @NotBlank(message = "O procedimento solicitado não deve estar em branco")
        String name,

        @NotBlank(message = "Qual a descrição do procedimento")
        String description,

        @NotNull(message = "Quantidade de cotas mensais")
        Integer montlyQuota
){}
