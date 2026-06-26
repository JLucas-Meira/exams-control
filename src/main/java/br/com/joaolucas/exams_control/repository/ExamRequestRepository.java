package br.com.joaolucas.exams_control.repository;

import br.com.joaolucas.exams_control.model.entities.ExamRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ExamRequestRepository extends JpaRepository<ExamRequest, UUID> {
}
