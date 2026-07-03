package br.com.joaolucas.exams_control.controller;

import br.com.joaolucas.exams_control.dto.request.ProcedureCreateDTO;
import br.com.joaolucas.exams_control.dto.response.ProcedureResponseDTO;
import br.com.joaolucas.exams_control.model.entities.Procedure;
import br.com.joaolucas.exams_control.service.ProcedureService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/procedures")
public class ProcedureController {

    private final ProcedureService procedureService;

    public ProcedureController(ProcedureService procedureService) {
        this.procedureService = procedureService;
    }

    @GetMapping
    public List<ProcedureResponseDTO> listProcedures(){
        return procedureService.listProcedures().stream().map(ProcedureResponseDTO::fromEntity).toList();
    }

    @GetMapping("/{id}")
    public ProcedureResponseDTO findById(@PathVariable UUID id){
        return ProcedureResponseDTO.fromEntity(procedureService.findById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProcedureResponseDTO createProcedure(@RequestBody ProcedureCreateDTO dto){
        Procedure savedProcedure = procedureService.createProcedure(dto);
        return ProcedureResponseDTO.fromEntity(savedProcedure);
    }
}
