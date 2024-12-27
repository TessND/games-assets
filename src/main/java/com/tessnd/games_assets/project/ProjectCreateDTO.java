package com.tessnd.games_assets.project;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ProjectCreateDTO {

    @NotBlank(message = "Название обязательно для заполнения")
    @Size(min = 5, max = 100, message = "Название должно быть от 5 до 100 символов")
    private String title;

    @NotBlank(message = "Описание обязательно для заполнения")
    @Size(min = 10, max = 500, message = "Описание должно быть от 10 до 500 символов")
    private String description;

    @NotNull(message = "ID типа проекта обязателен для заполнения")
    private Long projectTypeId;

    @NotNull(message = "Файл обязателен для заполнения")
    private MultipartFile file;
}

       