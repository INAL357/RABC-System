package com.app.auth.auth_app_backend.Exception;

public class ResourceNotFoundException extends RuntimeException{

    public ResourceNotFoundException(String message){
    super(message);
    }

    public ResourceNotFoundException(){
        super("Resource not found");
    }
}
