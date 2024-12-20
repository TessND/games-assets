package com.tessnd.games_assets.user.exceptions;

public class UsernameAlreadyTakenException extends RuntimeException{
    public UsernameAlreadyTakenException(String message) {
        super(message);
    }
}
