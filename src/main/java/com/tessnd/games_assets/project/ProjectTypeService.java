package com.tessnd.games_assets.project;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;

@Service

public class ProjectTypeService {

    @Autowired
    private ProjectTypeRepository projectTypeRepository;

    @PostConstruct
    public void init() {
        createProjectTypeIfNotFound("Game");
        createProjectTypeIfNotFound("Mod");
        createProjectTypeIfNotFound("Tool");
        createProjectTypeIfNotFound("Asset");
        
    }
        
    private void createProjectTypeIfNotFound(String string) {
        Optional<ProjectType> projectType = projectTypeRepository.findByName(string);
        if (!projectType.isPresent()) {
            ProjectType newType = new ProjectType();
            newType.setName(string);
            projectTypeRepository.save(newType);
        }           
    }
    
}
