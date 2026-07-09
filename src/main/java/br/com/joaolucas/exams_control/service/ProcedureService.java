package br.com.joaolucas.exams_control.service;

import br.com.joaolucas.exams_control.dto.request.ProcedureCreateDTO;
import br.com.joaolucas.exams_control.exception.ResourceAlreadyExistsException;
import br.com.joaolucas.exams_control.exception.ResourceNotFoundException;
import br.com.joaolucas.exams_control.model.entities.Procedure;
import br.com.joaolucas.exams_control.repository.ProcedureRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ProcedureService {

    private final ProcedureRepository procedureRepository;

    public ProcedureService (ProcedureRepository procedureRepository){
        this.procedureRepository = procedureRepository;
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
}
