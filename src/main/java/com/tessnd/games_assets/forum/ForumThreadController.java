package com.tessnd.games_assets.forum;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tessnd.games_assets.project.ProjectCreateDTO;
import com.tessnd.games_assets.user.UserService;
import com.tessnd.games_assets.user.exceptions.ForumThreadNotFoundException;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/forum")
public class ForumThreadController {
    
    @Autowired
    private ForumThreadService forumThreadService;

    @Autowired
    private ForumMessageService forumMessageService;

    @Autowired
    private UserService userService;


    @GetMapping("/list")
    public String showForumThreadList(Model model) {
        model.addAttribute("threads", forumThreadService.getAllForumThreads());
        return "forum_thread_list";
    }

    @GetMapping("/{id}")
    public String showForumThreadDetails(@PathVariable Long id, Model model, Principal principal) {
        try {
            model.addAttribute("isForumThreadOwner", forumThreadService.isForumThreadOwner(id, principal.getName()));
            model.addAttribute("thread", forumThreadService.getForumThreadById(id));
            model.addAttribute("forumMessages", forumMessageService.getAllByForumThread(forumThreadService.getForumThreadById(id)));
            return "forum_thread_details";
        } catch (ForumThreadNotFoundException e) {
            return "redirect:/forum/list";
        }
    }

    @GetMapping("/create")
    public String showThreadCreationForm(Model model) {
        try {
            model.addAttribute("thread", new ForumThreadCreateDTO());
            return "forum_thread_create";
        } catch (Exception e) {
            return "redirect:/forum/list";
        }
    }

    @PostMapping("/create")
    public String createThread(
            @Valid @ModelAttribute("thread") ForumThreadCreateDTO forumThreadCreateDTO,
            BindingResult bindingResult,
            Model model,
            Principal principal) {
        // Check for validation errors
        if (bindingResult.hasErrors()) {
            return "forum_thread_create";
        }

        try {
            // Save the thread with the current user's username
            forumThreadService.saveForumThread(forumThreadCreateDTO, principal.getName());
            return "redirect:/forum/list";
        } catch (Exception e) {
            model.addAttribute("error", "Ошибка при создании темы: " + e.getMessage());
            return "forum_thread_create";
        }
    }

    @GetMapping("/{id}/edit")
    public String showEditThreadForm(@PathVariable Long id, Model model, Principal principal) {
        try {
            // Check if the current user is the owner of the thread
            if (!forumThreadService.isThreadOwner(id, principal.getName())) {
                return "redirect:/forum/list";
            }

            // Fetch the thread by ID
            ForumThreadCreateDTO thread = new ForumThreadCreateDTO();
            model.addAttribute("threadIdToEdit", id); // Pass the thread ID to the form
            model.addAttribute("thread", new ForumThreadCreateDTO());
            return "forum_thread_edit";
        } catch (ForumThreadNotFoundException e) {
            return "redirect:/forum/list";
        }
    }

    @PostMapping("/{id}/edit")
    public String editThread(
            @PathVariable Long id,
            @Valid @ModelAttribute("thread") ForumThreadCreateDTO forumThreadCreateDTO,
            BindingResult bindingResult,
            Model model,
            Principal principal) {

        // Check if the current user is the owner of the thread
        if (!forumThreadService.isThreadOwner(id, principal.getName())) {
            return "redirect:/forum/list";
        }

        // Check for validation errors
        if (bindingResult.hasErrors()) {
            // If there are validation errors, return to the edit form with error messages
            model.addAttribute("threadIdToEdit", id); // Pass the thread ID to the form
            return "forum_thread_edit"; // Return to the edit form view
        }

        try {
            // If validation passes, edit the thread
            forumThreadService.editForumThread(id, forumThreadCreateDTO);
            return "redirect:/forum/list";
        } catch (ForumThreadNotFoundException e) {
            // Handle the case where the thread is not found
            return "redirect:/forum/list";
        }
    }

    @PostMapping("/{id}/delete")
    public String deleteThread(@PathVariable Long id, Principal principal, Model model) {
        // Check if the current user is the owner of the thread
        if (!forumThreadService.isThreadOwner(id, principal.getName())) {
            return "redirect:/forum/list";
        }
        try {
            forumThreadService.deleteForumThread(id);
            return "redirect:/forum/list";
        } catch (ForumThreadNotFoundException e) {
            return "redirect:/forum/list";
        }
    }
 
}
