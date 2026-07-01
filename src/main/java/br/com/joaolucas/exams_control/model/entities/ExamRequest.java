package br.com.joaolucas.exams_control.model.entities;

import br.com.joaolucas.exams_control.model.enums.Priority;
import br.com.joaolucas.exams_control.model.enums.Status;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "tb_exams")
public class ExamRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private LocalDate scheduledDate;

    @Enumerated(EnumType.STRING)
    private Priority priority;

    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "procedure_id")
    private Procedure procedure;

    @Column(updatable = false)
    private LocalDate createdAt;

    public ExamRequest() {
    }

    public ExamRequest(Patient patient, Procedure procedure, Priority priority) {
        this.patient = patient;
        this.procedure = procedure;
        this.priority = priority;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDate.now();
        this.status = Status.PENDING;
    }

    // --- Getters e Setters ---
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public LocalDate getScheduledDate() { return scheduledDate; }
    public void setScheduledDate(LocalDate scheduledDate) { this.scheduledDate = scheduledDate; }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Procedure getProcedure() {
        return procedure;
    }

    public void setProcedure(Procedure procedure) {
        this.procedure = procedure;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public Priority getPriority() { return priority; }
    public void setPriority(Priority priority) { this.priority = priority; }

    public Patient getPatient() { return patient; }
    public void setPatient(Patient patient) { this.patient = patient; }

    // --- Equals e HashCode (Apenas o ID) ---
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExamRequest exam = (ExamRequest) o;
        return Objects.equals(id, exam.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}