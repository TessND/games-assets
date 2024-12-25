package com.tessnd.games_assets.project;


import java.io.File;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.tessnd.games_assets.comment.CommentService;
import com.tessnd.games_assets.file.FileService;
import com.tessnd.games_assets.project.exceptions.ProjectNotFoundException;
import com.tessnd.games_assets.user.UserRepository;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;


@Service
@AllArgsConstructor
public class ProjectService {
    
    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommentService commentService;
    
    @Autowired
    private FileService fileService;

    @Autowired
    private ProjectTypeService projectTypeService;

    @Transactional
    public Project saveProject(ProjectCreateDTO project, String username) throws IOException{

        Project projectToSave = new Project();
        projectToSave.setTitle(project.getTitle());
        projectToSave.setDescription(project.getDescription());
        projectToSave.setLink(project.getLink());
        projectToSave.setProjectType(projectTypeService.getProjectTypeById(project.getProjectTypeId()));
        projectToSave.setUser(userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found")));
        projectToSave.setFilePath(fileService.save(project.getFile()));
        return projectRepository.save(projectToSave);
    }

    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    public Project getProjectById(Long id) {
        return projectRepository.findById(id).orElseThrow(() -> new ProjectNotFoundException("Project not found"));
    }

    @Transactional
    public void deleteProject(Long id) {
        if (!projectRepository.existsById(id)) {
            throw new ProjectNotFoundException("Project not found");
        }
        commentService.deleteAllByProjectId(id);
        projectRepository.deleteById(id);
    }

    @Transactional
    public Project editProject(Long id, ProjectCreateDTO project) throws IOException {
        Project projectToEdit = getProjectById(id);
        projectToEdit.setTitle(project.getTitle());
        projectToEdit.setDescription(project.getDescription());
        projectToEdit.setLink(project.getLink());
        projectToEdit.setUser(projectToEdit.getUser());
        projectToEdit.setFilePath(fileService.save(project.getFile()));
        return projectRepository.save(projectToEdit);
    }


    public boolean isProjectOwner(Long id, String name) {
        return projectRepository.findById(id).map(project -> project.getUser().getUsername().equals(name)).orElse(false);
    }


}
