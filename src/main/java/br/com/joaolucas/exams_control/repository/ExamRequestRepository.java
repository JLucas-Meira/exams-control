package br.com.joaolucas.exams_control.repository;

import br.com.joaolucas.exams_control.model.entities.ExamRequest;
import br.com.joaolucas.exams_control.model.entities.Procedure;
import br.com.joaolucas.exams_control.model.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.UUID;

@Repository
public interface ExamRequestRepository extends JpaRepository<ExamRequest, UUID> {
    long countByProcedureAndStatus (Procedure procedure, Status status);

    long countByProcedureAndStatusAndScheduledDateBetween(
            Procedure procedure,
            Status status,
            LocalDate startOfMonth,
            LocalDate endOfMonth
    );
}
