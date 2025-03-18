package com.leila.salaoBeleza.exceptions;

public class AdminAlreadyExistsException extends RuntimeException{
    public AdminAlreadyExistsException() {
        super("Um admin já foi cadastrado.");
    }
}
