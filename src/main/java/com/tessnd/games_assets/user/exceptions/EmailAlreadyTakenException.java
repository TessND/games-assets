package com.tessnd.games_assets.user.exceptions;

public class EmailAlreadyTakenException extends RuntimeException{
    public EmailAlreadyTakenException(String message) {
        super(message);
    }
}
