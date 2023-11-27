package com.sctp.todolist.exception;

public class ResourceNotFoundException extends RuntimeException{

    public ResourceNotFoundException(Long id){


        super("Could not find task " + id);
    }

    public ResourceNotFoundException() {
        super("No tasks found");
    }
}
