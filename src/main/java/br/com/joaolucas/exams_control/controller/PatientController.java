package br.com.joaolucas.exams_control.controller;

import br.com.joaolucas.exams_control.dto.request.PatientCreateDTO;
import br.com.joaolucas.exams_control.dto.response.PatientResponseDTO;
import br.com.joaolucas.exams_control.model.entities.Patient;
import br.com.joaolucas.exams_control.service.PatientService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/patients")
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @GetMapping
    public List<PatientResponseDTO> listPatients(){
        return patientService.listPatients().stream().map(PatientResponseDTO::fromEntity).toList();
    }

    @GetMapping("/{id}")
    public PatientResponseDTO findById(@PathVariable UUID id){
        return PatientResponseDTO.fromEntity(patientService.findById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PatientResponseDTO createPatient(@RequestBody PatientCreateDTO dto) {
        Patient savedPatient = patientService.createPatient(dto);
        return PatientResponseDTO.fromEntity(savedPatient);
    }

}
