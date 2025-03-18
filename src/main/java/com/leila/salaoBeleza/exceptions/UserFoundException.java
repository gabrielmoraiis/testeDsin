package com.leila.salaoBeleza.exceptions;

public class UserFoundException extends RuntimeException{
    public UserFoundException() {
        super("Email já cadastrado.");
    }
}
