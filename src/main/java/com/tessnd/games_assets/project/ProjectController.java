package com.tessnd.games_assets.project;

import java.io.IOException;
import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
import com.tessnd.games_assets.user.UserCreateDTO;
import com.tessnd.games_assets.user.UserService;
import com.tessnd.games_assets.user.exceptions.EmailAlreadyTakenException;
import com.tessnd.games_assets.user.exceptions.UsernameAlreadyTakenException;

import jakarta.validation.Valid;


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
    public String createProject(
            @Valid @ModelAttribute("project") ProjectCreateDTO projectCreateDTO,
            BindingResult bindingResult,
            Model model,
            Principal principal) { 
        // Check for validation errors
        if (bindingResult.hasErrors()) {
            model.addAttribute("projectTypes", projectTypeService.getAllProjectTypes());
            return "project_create";
        }

        try {
            // Process the file and save the project
            projectService.saveProject(projectCreateDTO, principal.getName());
            return "redirect:/project/list?success=true";
        } catch (Exception e) {
            model.addAttribute("error", "Ошибка при создании проекта: " + e.getMessage());
            model.addAttribute("projectTypes", projectTypeService.getAllProjectTypes());
            return "project_create";
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
    public String editProject(
            @PathVariable Long id,
            @Valid @ModelAttribute("project") ProjectCreateDTO project,
            BindingResult bindingResult,
            Model model,
            Principal principal) throws IOException {

        // Check if the current user is the owner of the project
        if (!projectService.isProjectOwner(id, principal.getName())) {
            return "redirect:/project/list";
        }

        // Check for validation errors
        if (bindingResult.hasErrors()) {
            // If there are validation errors, return to the edit form with error messages
            model.addAttribute("projectTypes", projectTypeService.getAllProjectTypes());
            model.addAttribute("projectIdToEdit", id); // Pass the project ID to the form
            return "project_edit"; // Return to the edit form view
        }

        try {
            // If validation passes, edit the project
            projectService.editProject(id, project);
            return "redirect:/project/list";
        } catch (ProjectNotFoundException e) {
            // Handle the case where the project is not found
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
    public String createComment(
            @PathVariable Long id,
            @Valid @ModelAttribute("comment") CommentCreateDTO comment,
            BindingResult bindingResult,
            Model model,
            Principal principal) {

        // Check for validation errors
        if (bindingResult.hasErrors()) {
            // If there are validation errors, return to the project details page with error messages
            model.addAttribute("project", projectService.getProjectById(id));
            model.addAttribute("comments", commentService.getAllByProject(projectService.getProjectById(id)));
            return "project_details"; // Return to the project details view
        }

        try {
            // If validation passes, save the comment
            commentService.save(comment, userService.getUserByUsername(principal.getName()), projectService.getProjectById(id));
            return "redirect:/project/" + id;
        } catch (ProjectNotFoundException e) {
            // Handle the case where the project is not found
            return "redirect:/project/list";
        }
    }

    // @GetMapping("/{id}/comments/delete/{commentId}")
    // public String deleteComment(@PathVariable Long id, @PathVariable Long commentId, Principal principal) {
    //     try {
    //         if (!commentService.isCommentOwner(commentId, principal.getName())) {
    //             return "redirect:/project/list";
    //         }
    //         commentService.delete(commentId, userService.getUserByUsername(principal.getName()), projectService.getProjectById(id));
    //         return "redirect:/project/" + id;
    //     } catch (ProjectNotFoundException e) {
    //         return "redirect:/project/list";
    //     }
    // }

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
}

        




    
    




