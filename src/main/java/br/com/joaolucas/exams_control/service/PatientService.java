package br.com.joaolucas.exams_control.service;

import br.com.joaolucas.exams_control.repository.PatientRepository;
import org.springframework.stereotype.Service;

@Service
public class PatientService {

    private PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }
}
