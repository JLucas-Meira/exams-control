package br.com.joaolucas.exams_control.exception;

public class ResourceAlreadyExistsException extends BusinessException{
    public ResourceAlreadyExistsException(String message){
        super(message);
    }
}
