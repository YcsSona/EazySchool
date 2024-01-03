package com.theonewhocode.eazyschool.controller;

import com.theonewhocode.eazyschool.model.Address;
import com.theonewhocode.eazyschool.model.Person;
import com.theonewhocode.eazyschool.model.Profile;
import com.theonewhocode.eazyschool.repository.PersonRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Controller(value = "profileControllerBean")
public class ProfileController {

    @Autowired
    private PersonRepository personRepository;

    @RequestMapping(value = "/displayProfile", method = RequestMethod.GET)
    public ModelAndView displayMessages(HttpSession session) {
        // Fetch logged-in user from HttpSession
        Person person = (Person) session.getAttribute("loggedInPerson");

        // Populating values into Profile using logged-in person
        Profile profile = new Profile();
        profile.setName(person.getName());
        profile.setEmail(person.getEmail());
        profile.setMobileNumber(person.getMobileNumber());

        if (person.getAddress() != null && person.getAddress().getAddressId() > 0) {
            profile.setAddress1(person.getAddress().getAddress1());
            profile.setAddress2(person.getAddress().getAddress2());
            profile.setCity(person.getAddress().getCity());
            profile.setState(person.getAddress().getState());
            profile.setZipCode(person.getAddress().getZipCode());
        }

        ModelAndView modelAndView = new ModelAndView("profile.html");
        modelAndView.addObject("profile", profile);
        return modelAndView;
    }

    @RequestMapping(value = "/updateProfile", method = RequestMethod.POST)
    public String updateProfile(@Valid @ModelAttribute("profile") Profile profile, Errors errors, HttpSession session) {
        if (errors.hasErrors()) {
            return "profile.html";
        }

        Person person = (Person) session.getAttribute("loggedInPerson");
        person.setName(profile.getName());
        person.setEmail(profile.getEmail());
        person.setMobileNumber(profile.getMobileNumber());

        // If logged-in person does not have any address details, create new address to avoid NullPointerException
        if (person.getAddress() == null || !(person.getAddress().getAddressId() > 0)) {
            person.setAddress(new Address());
        }

        person.getAddress().setAddress1(profile.getAddress1());
        person.getAddress().setAddress2(profile.getAddress2());
        person.getAddress().setCity(profile.getCity());
        person.getAddress().setState(profile.getState());
        person.getAddress().setZipCode(profile.getZipCode());

        // save entity into the DB
        personRepository.save(person);

        // update logged-in person details in HttpSession
        session.setAttribute("loggedInPerson", person);

        return "redirect:/displayProfile";
    }
}
