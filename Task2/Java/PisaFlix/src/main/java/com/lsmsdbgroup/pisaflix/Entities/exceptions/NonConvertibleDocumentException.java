package com.lsmsdbgroup.pisaflix.Entities.exceptions;

public class NonConvertibleDocumentException extends Exception{
    
    public NonConvertibleDocumentException(String errorMessage) {
        super(errorMessage);
    }
}
