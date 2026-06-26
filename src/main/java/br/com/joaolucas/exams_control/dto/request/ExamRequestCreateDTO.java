package br.com.joaolucas.exams_control.dto.request;

import br.com.joaolucas.exams_control.model.enums.Priority;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ExamRequestCreateDTO(

        @NotNull(message = "O ID do paciente é obrigatório")
        UUID patientId,

        @NotNull(message = "O ID do procedimento é obrigatório")
        UUID procedureId,

        @NotNull(message = "Qual o nível de prioridade da solicitação?")
        Priority priority
){}
