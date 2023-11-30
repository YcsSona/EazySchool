package com.theonewhocode.eazyschool.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

    @RequestMapping(value = {"", "/", "home"})
    public String displayHomePage(Model model) {
        // Model interface: container between UI and backend
        model.addAttribute("username", "Jane Doe");
        return "home.html";
    }
}
