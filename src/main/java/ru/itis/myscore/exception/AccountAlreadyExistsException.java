package ru.itis.myscore.exception;

public class AccountAlreadyExistsException extends RuntimeException{
    public AccountAlreadyExistsException(String message) {
        super(message);
    }
}
