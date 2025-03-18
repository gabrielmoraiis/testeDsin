package com.leila.salaoBeleza.exceptions;

public class ServicesNotFoundException extends RuntimeException{
    public ServicesNotFoundException() {
        super("Serviço não encontrado");
    }
}
