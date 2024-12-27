package com.tessnd.games_assets.forum;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ForumMessageCreateDTO {
    @NotBlank(message = "Необходимо название")
    private String content;
}
