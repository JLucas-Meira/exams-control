package br.com.joaolucas.exams_control.models.entities;


import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "tb_patients")
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;
    private LocalDate birthDate;

    // A única trava de banco de dados essencial para evitar duplicidade de cadastros
    @Column(unique = true)
    private String susCard;

    private String phoneNumber;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Exam> exams = new ArrayList<>();

    public Patient() {
    }
    public Patient(String name, LocalDate birthDate, String susCard, String phoneNumber) {
        this.name = name;
        this.birthDate = birthDate;
        this.susCard = susCard;
        this.phoneNumber = phoneNumber;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    // --- Getters e Setters ---
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public LocalDate getBirthDate() { return birthDate; }
    public void setBirthDate(LocalDate birthDate) { this.birthDate = birthDate; }

    public String getSusCard() { return susCard; }
    public void setSusCard(String susCard) { this.susCard = susCard; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public LocalDateTime getCreatedAt() { return createdAt; }

    public List<Exam> getExams() { return exams; }

    // Utilitário para adicionar exames com segurança
    public void addExam(Exam exam) {
        this.exams.add(exam);
        exam.setPatient(this);
    }

    // --- Equals e HashCode (Apenas o ID) ---
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Patient patient = (Patient) o;
        return Objects.equals(id, patient.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}