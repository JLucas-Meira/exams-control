package br.com.joaolucas.exams_control.dto.response;

public record DashboardProcedureDTO(
        String procedureName,
        int totalQuota,
        long scheduledPatients,
        long availableQuota,
        long patientsInQueue
) {
}
