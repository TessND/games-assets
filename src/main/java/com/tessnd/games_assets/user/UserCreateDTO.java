package com.tessnd.games_assets.user;

import lombok.Data;

@Data
public class UserCreateDTO {
    private String username;
    private String password;
    private String email;
}
