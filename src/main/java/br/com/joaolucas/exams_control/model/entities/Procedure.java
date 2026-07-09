package br.com.joaolucas.exams_control.model.entities;

import jakarta.persistence.*;

import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "tb_procedures")
public class Procedure {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;
    private String description;
    private Integer montlyQuota;

    public Procedure(){
    }

    public Procedure(String name, String description, Integer montlyQuota) {
        this.name = name;
        this.description = description;
        this.montlyQuota = montlyQuota;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getMontlyQuota() {
        return montlyQuota;
    }

    public void setMontlyQuota(Integer montlyQuota) {
        this.montlyQuota = montlyQuota;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Procedure procedure = (Procedure) o;
        return Objects.equals(id, procedure.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
