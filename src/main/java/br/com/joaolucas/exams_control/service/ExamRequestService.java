package br.com.joaolucas.exams_control.service;

import br.com.joaolucas.exams_control.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import br.com.joaolucas.exams_control.dto.request.ExamRequestCreateDTO;
import br.com.joaolucas.exams_control.model.entities.ExamRequest;
import br.com.joaolucas.exams_control.model.entities.Patient;
import br.com.joaolucas.exams_control.model.entities.Procedure;
import br.com.joaolucas.exams_control.repository.ExamRequestRepository;

import java.util.List;
import java.util.UUID;

@Service
public class ExamRequestService {

    private final PatientService patientService;
    private final ProcedureService procedureService;
    private final ExamRequestRepository examRequestRepository;

    public ExamRequestService(PatientService patientService, ProcedureService procedureService, ExamRequestRepository examRequestRepository) {
        this.patientService = patientService;
        this.procedureService = procedureService;
        this.examRequestRepository = examRequestRepository;
    }

    public ExamRequest createExamRequest(ExamRequestCreateDTO dto){
        Patient patient = patientService.findById(dto.patientId());
        Procedure procedure = procedureService.findById(dto.procedureId());

        ExamRequest newExamRequest = new ExamRequest(patient, procedure, dto.priority());
        return examRequestRepository.save(newExamRequest);
    }

    public List<ExamRequest> listExamRequest (){
        return examRequestRepository.findAll();
    }

    public ExamRequest findById(UUID id){
        return examRequestRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Solicitação de exame não encontrada"));
    }
}
