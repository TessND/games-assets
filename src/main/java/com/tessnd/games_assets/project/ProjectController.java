package com.tessnd.games_assets.project;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/project")
public class ProjectController {
    
    @Autowired
    private ProjectService projectService;

    @GetMapping("/list")
    public String showProjectList(Model model) {
        model.addAttribute("projects", projectService.getAllProjects());
        return "project_list";
    }

    @GetMapping("/{id}")
    public String showProjectDetails(@PathVariable Long id, Model model, Principal principal) {
        model.addAttribute("isProjectOwner", projectService.isProjectOwner(id, principal.getName()));
        model.addAttribute("project", projectService.getProjectById(id));
        return "project_details";
    }


    @GetMapping("/create")
    public String showProjectCreationForm(Model model) {
        model.addAttribute("project", new Project());
        return "project_create";
    }

    @PostMapping("/create")
    public String createProject(@ModelAttribute ProjectCreateDTO project, Model model, Principal principal) {
        projectService.saveProject(project, principal.getName());
        return "redirect:/project/list";
    }

    @GetMapping("/{id}/edit")
    public String showProjectEditForm(@PathVariable Long id, Model model, Principal principal) {
        if (!projectService.isProjectOwner(id, principal.getName())) {
            return "redirect:/project/list";
        }
        model.addAttribute("project", projectService.getProjectById(id));
        return "project_edit";
    }

    @PostMapping("/{id}/edit")
    public String editProject(@PathVariable Long id, @ModelAttribute ProjectCreateDTO project, Model model, Principal principal) {
        if (!projectService.isProjectOwner(id, principal.getName())) {
            return "redirect:/project/list";
        }
        projectService.editProject(id, project);
        return "redirect:/project/list";
    }

    @GetMapping("/{id}/delete")
    public String deleteProject(@PathVariable Long id, Principal principal, Model model) {
        if (!projectService.isProjectOwner(id, principal.getName())) {
            return "redirect:/project/list";
        }
        projectService.deleteProject(id);
        return "redirect:/project/list";
    }
    
    



}
