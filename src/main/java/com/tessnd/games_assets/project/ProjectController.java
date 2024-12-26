package com.tessnd.games_assets.project;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.tessnd.games_assets.comment.Comment;
import com.tessnd.games_assets.comment.CommentCreateDTO;
import com.tessnd.games_assets.comment.CommentService;
import com.tessnd.games_assets.file.FileService;
import com.tessnd.games_assets.project.exceptions.ProjectNotFoundException;
import com.tessnd.games_assets.user.UserService;


@Controller
@RequestMapping("/project")
public class ProjectController {
    
    @Autowired
    private ProjectService projectService;


    @Autowired
    private UserService userService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private FileService fileService;

    @Autowired
    private ProjectTypeService projectTypeService;

    @GetMapping("/list")
    public String showProjectList(Model model) {
        model.addAttribute("projects", projectService.getAllProjects());
        return "project_list";
    }

    @GetMapping("/{id}")
    public String showProjectDetails(@PathVariable Long id, Model model, Principal principal) {
        try {
            model.addAttribute("isProjectOwner", projectService.isProjectOwner(id, principal.getName()));
            model.addAttribute("project", projectService.getProjectById(id));
            model.addAttribute("comments", commentService.getAllByProject(projectService.getProjectById(id)));
            return "project_details";
        }
        catch (ProjectNotFoundException e) {
            return "redirect:/project/list";
        }
        
    }


    @GetMapping("/create")
    public String showProjectCreationForm(Model model) {
        try {
            model.addAttribute("project", new ProjectCreateDTO());
            model.addAttribute("projectTypes", projectTypeService.getAllProjectTypes());
            return "project_create";
        } catch (Exception e) {
            return "redirect:/project/list";
        }
    }

    @PostMapping("/create")
    public String createProject(@ModelAttribute ProjectCreateDTO project, Model model, Principal principal) throws IOException {
        projectService.saveProject(project, principal.getName());
        return "redirect:/project/list";
    }

    @GetMapping("/{id}/download")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long id, Model model) throws IOException {
        try {
            String filePath = projectService.getProjectById(id).getFilePath();
            Resource fileResource = fileService.load(filePath);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileResource.getFilename() + "\"")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(fileResource);
        } catch (ProjectNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
        
    }

    @GetMapping("/{id}/edit")
    public String showProjectEditForm(@PathVariable Long id, Model model, Principal principal) {
        if (!projectService.isProjectOwner(id, principal.getName())) {
            return "redirect:/project/list";
        }
        try {
            model.addAttribute("projectIdToEdit", id);
            model.addAttribute("project", new ProjectCreateDTO());
            model.addAttribute("projectTypes", projectTypeService.getAllProjectTypes());
            return "project_edit";
        } catch (ProjectNotFoundException e) {
            return "redirect:/project/list";
        }
        
    }

    @PostMapping("/{id}/edit")
    public String editProject(@PathVariable Long id, @ModelAttribute ProjectCreateDTO project, Model model, Principal principal) throws IOException {
        if (!projectService.isProjectOwner(id, principal.getName())) {
            return "redirect:/project/list";
        }
        try {
            projectService.editProject(id, project);
            return "redirect:/project/list";
        } catch (ProjectNotFoundException e) {
            return "redirect:/project/list";
        }
        
    }

    @GetMapping("/{id}/delete")
    public String deleteProject(@PathVariable Long id, Principal principal, Model model) throws IOException {
        if (!projectService.isProjectOwner(id, principal.getName())) {
            return "redirect:/project/list";
        }
        try {
            projectService.deleteProject(id);
            return "redirect:/project/list";
        } catch (ProjectNotFoundException e) {
            return "redirect:/project/list";
        }
        
    }

    @GetMapping("/{id}/comments/create")
    public String showCommentCreationForm(@PathVariable Long id, Model model) {
        try {
            model.addAttribute("comment", new Comment());
            model.addAttribute("project", projectService.getProjectById(id));
            return "comment_create";
        } catch (ProjectNotFoundException e) {
            return "redirect:/project/list";
        }
       
    }


    @PostMapping("/{id}/comments/create")
    public String createComment(@PathVariable Long id, @ModelAttribute CommentCreateDTO comment, Model model, Principal principal) {
        try {
            commentService.save(comment, userService.getUserByUsername(principal.getName()), projectService.getProjectById(id));
            return "redirect:/project/" + id;
        }
        catch (ProjectNotFoundException e) {
            return "redirect:/project/list";
        }
        
    }


        




    
    



}
