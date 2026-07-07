package br.com.joaolucas.exams_control.exception;

public abstract class BusinessException extends RuntimeException{
    public BusinessException(String message){
        super(message);
    }
}
