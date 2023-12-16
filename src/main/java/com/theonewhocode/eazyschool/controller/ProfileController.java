package com.theonewhocode.eazyschool.controller;

import com.theonewhocode.eazyschool.model.Profile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Controller
public class ProfileController {

    @RequestMapping(value = "/displayProfile", method = RequestMethod.GET)
    public ModelAndView displayMessages(Model model) {
        Profile profile = new Profile();
        ModelAndView modelAndView = new ModelAndView("profile.html");
        modelAndView.addObject("profile", profile);
        return modelAndView;
    }
}
