package com.tessnd.games_assets.project;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;

@Service

public class ProjectTypeService {

    @Autowired
    private ProjectTypeRepository projectTypeRepository;
    
    public List<ProjectType> getAllProjectTypes() {
        return projectTypeRepository.findAll();
    }

    public ProjectType getProjectTypeById(Long id) {
        return projectTypeRepository.findById(id).orElse(null);
    }

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
