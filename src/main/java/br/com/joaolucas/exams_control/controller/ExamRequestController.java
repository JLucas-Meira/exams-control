package br.com.joaolucas.exams_control.controller;

import br.com.joaolucas.exams_control.dto.request.ExamRequestCreateDTO;
import br.com.joaolucas.exams_control.dto.response.ExamRequestResponseDTO;
import br.com.joaolucas.exams_control.model.entities.ExamRequest;
import br.com.joaolucas.exams_control.service.ExamRequestService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/exam-requests")
public class ExamRequestController {

    private final ExamRequestService examRequestService;

    public ExamRequestController(ExamRequestService examRequestService) {
        this.examRequestService = examRequestService;
    }

    @GetMapping
    public List<ExamRequestResponseDTO> listExamRequests (){
        return examRequestService.listExamRequest().stream().map(ExamRequestResponseDTO::fromEntity).toList();
    }

    @GetMapping("/{id}")
    public ExamRequestResponseDTO findById (@PathVariable UUID id){
        return ExamRequestResponseDTO.fromEntity(examRequestService.findById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ExamRequestResponseDTO createExamRequest(@RequestBody ExamRequestCreateDTO dto){
        ExamRequest savedExamRequest = examRequestService.createExamRequest(dto);
        return ExamRequestResponseDTO.fromEntity(savedExamRequest);
    }
}
