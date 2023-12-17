package com.theonewhocode.eazyschool.controller;

import com.theonewhocode.eazyschool.model.EazyClass;
import com.theonewhocode.eazyschool.repository.EazyClassRepository;
import com.theonewhocode.eazyschool.repository.PersonRepository;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
@RequestMapping("admin")
public class AdminController {

    @Autowired
    private EazyClassRepository eazyClassRepository;

    @Autowired
    private PersonRepository personRepository;

    @RequestMapping("/displayClasses")
    public ModelAndView displayClasses() {
        List<EazyClass> eazyClasses = eazyClassRepository.findAll();
        ModelAndView modelAndView = new ModelAndView("classes.html");
        modelAndView.addObject("eazyClasses", eazyClasses);
        modelAndView.addObject("eazyClass", new EazyClass());
        return modelAndView;
    }

    @RequestMapping(value = "/addNewClass", method = RequestMethod.POST)
    public String addNewClass(@Valid @ModelAttribute("eazyClass") EazyClass eazyClass) {
        eazyClassRepository.save(eazyClass);
        return "redirect:/admin/displayClasses";
    }

    @RequestMapping("/deleteClass")
    public String deleteClass(@RequestParam int id) {
        Optional<EazyClass> optionalEazyClass = eazyClassRepository.findById(id);

        // Remove link between class & person
        optionalEazyClass.ifPresent(eazyClass -> eazyClass.getPersons().forEach(person -> {
            person.setEazyClass(null);
            personRepository.save(person);
        }));

        eazyClassRepository.deleteById(id);
        return "redirect:/admin/displayClasses";
    }

    @RequestMapping(value = "/displayStudents", method = RequestMethod.GET)
    public ModelAndView displayStudents(@RequestParam int classId) {
        ModelAndView modelAndView = new ModelAndView("students.html");
        return modelAndView;
    }
}
