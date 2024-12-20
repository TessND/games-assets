package com.tessnd.games_assets.user;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tessnd.games_assets.user.exceptions.EmailAlreadyTakenException;
import com.tessnd.games_assets.user.exceptions.UsernameAlreadyTakenException;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new UserCreateDTO());
        return "register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute UserCreateDTO user, Model model){
        try {
            userService.registerUser(user);
            return "redirect:/login";
        } catch (UsernameAlreadyTakenException | EmailAlreadyTakenException e) {
            model.addAttribute("error", e.getMessage());
            return "redirect:/user/register?error=" + e.getMessage();
        }
    }
}
