package br.com.joaolucas.exams_control.exception;

public class ResourceNotFoundException extends BusinessException{
    public ResourceNotFoundException(String message){
        super(message);
    }
}
