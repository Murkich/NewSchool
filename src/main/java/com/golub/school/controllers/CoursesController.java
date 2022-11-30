package com.golub.school.controllers;

import com.golub.school.entity.Courses;
import com.golub.school.service.CoursesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/courses")
public class CoursesController {
    @Autowired
    CoursesService coursesService;

    @GetMapping()
    public String showCourses(Model model) {
        model.addAttribute("courses", coursesService.getAllCourse());
        model.addAttribute("course", new Courses());
        return "courses";
    }

    @RequestMapping(value = "/eng", method = RequestMethod.GET)
    public String showEnglishCourses(Model model) {
        model.addAttribute("courses", coursesService.findCoursesByLanguage("Английский"));
        model.addAttribute("course", new Courses());
        return "courses";
    }

    @RequestMapping(value = "/germ", method = RequestMethod.GET)
    public String showGermanCourses(Model model) {
        model.addAttribute("courses", coursesService.findCoursesByLanguage("Немецкий"));
        model.addAttribute("course", new Courses());
        return "courses";
    }

    @RequestMapping(value = "/french", method = RequestMethod.GET)
    public String showFrenchCourses(Model model) {
        model.addAttribute("courses", coursesService.findCoursesByLanguage("Французский"));
        model.addAttribute("course", new Courses());
        return "courses";
    }
}
