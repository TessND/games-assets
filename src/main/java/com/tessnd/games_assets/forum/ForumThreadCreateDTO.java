package com.tessnd.games_assets.forum;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ForumThreadCreateDTO {
    @NotBlank(message = "Необходимо указать тему")
    @Size(min = 3, max = 50, message = "Название должно быть от 3 до 50 символов")
    private String title;
    @NotBlank(message = "Необходимо описание")
    private String description;
}