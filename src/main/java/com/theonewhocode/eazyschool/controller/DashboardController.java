package com.theonewhocode.eazyschool.controller;

import com.theonewhocode.eazyschool.model.Person;
import com.theonewhocode.eazyschool.repository.PersonRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class DashboardController {

    @Autowired
    private PersonRepository personRepository;

    @RequestMapping("/dashboard")
    public String displayDashboard(Model model, Authentication authentication, HttpSession session) {
        // UsernamePasswordAuthenticationToken should send email as there can be multiple users with the same name
        Person person = personRepository.readByEmail(authentication.getName());
        // setting name from the fetched entity from the DB
        model.addAttribute("username", person.getName());
        model.addAttribute("roles", authentication.getAuthorities().toString());

        if (person.getEazyClass() != null && person.getEazyClass().getName() != null) {
            model.addAttribute("enrolledClass", person.getEazyClass().getName());
        }

        session.setAttribute("loggedInPerson", person);

        return "dashboard.html";
    }
}
