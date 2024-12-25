package com.tessnd.games_assets.project;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class ProjectCreateDTO {
    private String title;
    private String description;
    private String link;
    private Long projectTypeId;
    private MultipartFile file;
}
       