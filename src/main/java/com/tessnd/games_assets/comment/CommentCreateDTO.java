package com.tessnd.games_assets.comment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;


@Data
public class CommentCreateDTO {

    @NotBlank(message = "Комментарий не может быть пустым")
    @Size(min = 1, max = 500, message = "Комментарий должен быть от 1 до 500 символов")
    private String text;
}
