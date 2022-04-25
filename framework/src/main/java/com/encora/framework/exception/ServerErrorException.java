package com.encora.framework.exception;

public class ServerErrorException extends Exception {
    public ServerErrorException(String errorMessage){
        super(errorMessage);
    }
}
