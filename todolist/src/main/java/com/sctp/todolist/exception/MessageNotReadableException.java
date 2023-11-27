package com.sctp.todolist.exception;

public class MessageNotReadableException extends RuntimeException{

    public MessageNotReadableException(){

        super("Request is malformed. Please check again.");

    }

}
