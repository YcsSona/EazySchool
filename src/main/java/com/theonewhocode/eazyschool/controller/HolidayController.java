package com.theonewhocode.eazyschool.controller;

import com.theonewhocode.eazyschool.model.Holiday;
import com.theonewhocode.eazyschool.repository.HolidaysRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class HolidayController {

    @Autowired
    private HolidaysRepository holidaysRepository;

    @GetMapping("/holidays/{display}")
    public String displayHolidays(@PathVariable String display, Model model) {

        if ("all".equals(display)) {
            model.addAttribute("festival", true);
            model.addAttribute("federal", true);
        } else if ("festival".equals(display)) {
            model.addAttribute("festival", true);

        } else if ("federal".equals(display)) {
            model.addAttribute("federal", true);

        }

        // As this is just a method invocation so need of SERVICE layer
        List<Holiday> holidays = holidaysRepository.findAllHolidays();

        Holiday.Type[] types = Holiday.Type.values();
        for (Holiday.Type type : types) {
            model.addAttribute(type.toString(),
                    (holidays.stream().filter(holiday -> holiday.getType().equals(type)).collect(Collectors.toList())));
        }
        return "holidays.html";
    }
}
