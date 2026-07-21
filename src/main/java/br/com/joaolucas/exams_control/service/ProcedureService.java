package br.com.joaolucas.exams_control.service;

import br.com.joaolucas.exams_control.dto.request.ProcedureCreateDTO;
import br.com.joaolucas.exams_control.dto.response.DashboardProcedureDTO;
import br.com.joaolucas.exams_control.exception.ResourceAlreadyExistsException;
import br.com.joaolucas.exams_control.exception.ResourceNotFoundException;
import br.com.joaolucas.exams_control.model.entities.Procedure;
import br.com.joaolucas.exams_control.model.enums.Status;
import br.com.joaolucas.exams_control.repository.ExamRequestRepository;
import br.com.joaolucas.exams_control.repository.ProcedureRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ProcedureService {

    private final ProcedureRepository procedureRepository;
    private final ExamRequestRepository examRequestRepository;

    public ProcedureService (ProcedureRepository procedureRepository, ExamRequestRepository examRequestRepository){
        this.procedureRepository = procedureRepository;
        this.examRequestRepository = examRequestRepository;
    }

    public Procedure createProcedure(ProcedureCreateDTO dto){
        if(procedureRepository.existsByName(dto.name())){
            throw new ResourceAlreadyExistsException("Procedimento já cadastrado no sistema");
        }

        Procedure newProcedure = new Procedure(dto.name(), dto.description(), dto.montlyQuota());
        return procedureRepository.save(newProcedure);
    }

    public List<Procedure> listProcedures(){
        return procedureRepository.findAll();
    }

    public Procedure findById(UUID id){
        return procedureRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Procedimento não encontrado"));
    }

    public List<DashboardProcedureDTO> getDashboardProcedures(){
        LocalDate startOfMonth = YearMonth.now().atDay(1);
        LocalDate endOfMonth = YearMonth.now().atEndOfMonth();

        List<Procedure> procedures = procedureRepository.findAll();

        List<DashboardProcedureDTO> dashboard = new ArrayList<>();

        for(Procedure procedure : procedures){
            long patientsInQueue = examRequestRepository.countByProcedureAndStatus(procedure, Status.PENDING);
            long scheduledPatients = examRequestRepository.countByProcedureAndStatusAndScheduledDateBetween(procedure, Status.SCHEDULED, startOfMonth, endOfMonth);
            long availableQuota = procedure.getMontlyQuota() - scheduledPatients;
            if (availableQuota < 0){
                availableQuota = 0;
            }
            dashboard.add(new DashboardProcedureDTO(procedure.getName(), procedure.getMontlyQuota(), scheduledPatients, availableQuota, patientsInQueue));
        }
        return dashboard;
    }
}
