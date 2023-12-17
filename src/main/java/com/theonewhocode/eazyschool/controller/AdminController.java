package com.theonewhocode.eazyschool.controller;

import com.theonewhocode.eazyschool.model.EazyClass;
import com.theonewhocode.eazyschool.model.Person;
import com.theonewhocode.eazyschool.repository.EazyClassRepository;
import com.theonewhocode.eazyschool.repository.PersonRepository;
import jakarta.servlet.http.HttpSession;
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
    public ModelAndView displayStudents(@RequestParam int classId,
                                        @RequestParam(value = "error", required = false) String error,
                                        HttpSession session) {
        ModelAndView modelAndView = new ModelAndView("students.html");

        Optional<EazyClass> optionalEazyClass = eazyClassRepository.findById(classId);
        optionalEazyClass.ifPresent(eazyClass -> {
            modelAndView.addObject("eazyClass", eazyClass);
            // Required when admin is adding new students details
            session.setAttribute("eazyClass", eazyClass);
        });

        modelAndView.addObject("person", new Person());

        if (error != null) {
            modelAndView.addObject("errorMessage", "Invalid Email entered!!");
        }

        return modelAndView;
    }

    @RequestMapping(value = "/addStudent", method = RequestMethod.POST)
    // Do not use @Valid annotation to avoid validation related errors on Person entity
    public ModelAndView addStudent(@ModelAttribute("person") Person person, HttpSession session) {
        ModelAndView modelAndView = new ModelAndView();

        EazyClass eazyClass = (EazyClass) session.getAttribute("eazyClass");
        Person personEntity = personRepository.readByEmail(person.getEmail());

        if (personEntity == null || !(personEntity.getPersonId() > 0)) {
            modelAndView.setViewName("redirect:/admin/displayStudents?classId=" + eazyClass.getClassId() + "&error=true");
            return modelAndView;
        }

        // Link class with person
        personEntity.setEazyClass(eazyClass);
        personRepository.save(personEntity);

        // Link person with class
        eazyClass.getPersons().add(personEntity);
        eazyClassRepository.save(eazyClass);

        modelAndView.setViewName("redirect:/admin/displayStudents?classId=" + eazyClass.getClassId());
        return modelAndView;
    }

    @RequestMapping("/deleteStudent")
    public ModelAndView deleteStudent(@RequestParam int personId, HttpSession session) {
        EazyClass eazyClass = (EazyClass) session.getAttribute("eazyClass");

        Optional<Person> optionalPerson = personRepository.findById(personId);
        optionalPerson.ifPresent(person -> {
            person.setEazyClass(null);
            eazyClass.getPersons().remove(person);
            EazyClass eazyClassSaved = eazyClassRepository.save(eazyClass);
            session.setAttribute("eazyClass", eazyClassSaved);
        });


        ModelAndView modelAndView = new ModelAndView("redirect:/admin/displayStudents?classId=" + eazyClass.getClassId());
        return modelAndView;
    }
}
