package com.tessnd.games_assets.home;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HomeController {
    @GetMapping
    public String home(Model model) {
        return "home";
    }
    
    @GetMapping("/secret_page")
    public String secretPage(Model model) {
        return "secret_page";
    }
}
