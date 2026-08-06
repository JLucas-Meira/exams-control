package br.com.joaolucas.exams_control.service;

import br.com.joaolucas.exams_control.dto.request.PatientCreateDTO;
import br.com.joaolucas.exams_control.dto.request.PatientPatchDTO;
import br.com.joaolucas.exams_control.exception.ResourceAlreadyExistsException;
import br.com.joaolucas.exams_control.exception.ResourceNotFoundException;
import br.com.joaolucas.exams_control.model.entities.Patient;
import br.com.joaolucas.exams_control.repository.PatientRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PatientService {

    private final PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public Patient createPatient (PatientCreateDTO dto){

        if (patientRepository.existsBySusCard(dto.susCard())){
            throw new ResourceAlreadyExistsException("Número do cartão do SUS, já cadastrado no sistema!");
        }

        Patient newPatient = new Patient(dto.name(), dto.birthDate(), dto.susCard(), dto.phoneNumber());

        return patientRepository.save(newPatient);
    }
    
    public List<Patient> listPatients(){
        return patientRepository.findAll();
    }
    
    public Patient findById(UUID id){
        return patientRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Paciente não encontrado"));
    }

    public Patient updatePatient(UUID id, PatientPatchDTO patchDTO){
        Patient patient = findById(id);

        if(patchDTO.name() != null){
            patient.setName(patchDTO.name());
        }
        if(patchDTO.birthDate() != null){
            patient.setBirthDate(patchDTO.birthDate());
        }
        if(patchDTO.susCard() != null){
            patient.setSusCard(patchDTO.susCard());
        }
        if(patchDTO.phoneNumber() != null){
            patient.setPhoneNumber(patchDTO.phoneNumber());
        }

        patientRepository.save(patient);
        return patient;
    }
}
