package com.tessnd.games_assets.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserCreateDTO {
    // @NotBlank(message = "Имя пользователя не может быть пустым")
    // @Size(min = 3, max = 50, message = "Имя пользователя должно быть от 3 до 50 символов")
    private String username;

    // @NotBlank(message = "Пароль не может быть пустым")
    // @Size(min = 8, max = 100, message = "Пароль должен быть от 8 до 100 символов")
    private String password;

    // @NotBlank(message = "Email не может быть пустым")
    // @Email(message = "Неверный формат email")
    private String email;
}
