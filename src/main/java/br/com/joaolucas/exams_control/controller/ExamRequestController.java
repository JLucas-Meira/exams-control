package br.com.joaolucas.exams_control.controller;

import br.com.joaolucas.exams_control.dto.request.ExamRequestCreateDTO;
import br.com.joaolucas.exams_control.dto.response.ExamRequestResponseDTO;
import br.com.joaolucas.exams_control.model.entities.ExamRequest;
import br.com.joaolucas.exams_control.service.ExamRequestService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ExamRequestResponseDTO createExamRequest(@RequestBody ExamRequestCreateDTO dto){
        ExamRequest savedExamRequest = examRequestService.createExamRequest(dto);
        return ExamRequestResponseDTO.fromEntity(savedExamRequest);
    }
}
