package com.golub.school.controllers;

import com.golub.school.entity.Courses;
import com.golub.school.entity.Users;
import com.golub.school.service.CoursesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.Date;
import java.text.DateFormat;
import java.util.Map;
import java.util.Objects;

@Controller
@RequestMapping("/courses")
public class CoursesController {
    private static final String path = "adminsTemplates/";
    @Autowired
    CoursesService coursesService;
    public String getRole() {
        String role = SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString();
        if(Objects.equals(role, "[ROLE_ANONYMOUS]")) return  "anonymous";
        else if(Objects.equals(role, "[USER]")) return  "user";
        else if(Objects.equals(role, "[ADMIN]")) return  "admin";
        return "anonymous";
    }

    @GetMapping()
    public String showCourses(Model model) {
        model.addAttribute("courses", coursesService.getAllCourse());
        model.addAttribute("course", new Courses());
        model.addAttribute("user_role", getRole());
        if (getRole().equals("anonymous"));
        else {
            Users user = (Users)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            model.addAttribute("user_avatar", "/img/" + user.getAvatar().getData());
        }
        return "courses";
    }

    @RequestMapping(value = "/eng", method = RequestMethod.GET)
    public String showEnglishCourses(Model model) {
        model.addAttribute("courses", coursesService.findCoursesByLanguage("Английский"));
        model.addAttribute("course", new Courses());
        model.addAttribute("user_role", getRole());
        if (getRole().equals("anonymous"));
        else {
            Users user = (Users)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            model.addAttribute("user_avatar", "/img/" + user.getAvatar().getData());
        }
        return "courses";
    }

    @RequestMapping(value = "/germ", method = RequestMethod.GET)
    public String showGermanCourses(Model model) {
        model.addAttribute("courses", coursesService.findCoursesByLanguage("Немецкий"));
        model.addAttribute("course", new Courses());
        model.addAttribute("user_role", getRole());
        if (getRole().equals("anonymous"));
        else {
            Users user = (Users)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            model.addAttribute("user_avatar", "/img/" + user.getAvatar().getData());
        }
        return "courses";
    }

    @RequestMapping(value = "/french", method = RequestMethod.GET)
    public String showFrenchCourses(Model model) {
        model.addAttribute("courses", coursesService.findCoursesByLanguage("Французский"));
        model.addAttribute("course", new Courses());
        model.addAttribute("user_role", getRole());
        if (getRole().equals("anonymous"));
        else {
            Users user = (Users)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            model.addAttribute("user_avatar", "/img/" + user.getAvatar().getData());
        }
        return "courses";
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public String showSearchCourses(Model model, @ModelAttribute("course") Courses course) {
        model.addAttribute("courses", coursesService.findCoursesByNameContaining(course.getName()));
        model.addAttribute("course", new Courses());
        model.addAttribute("user_role", getRole());
        if (getRole().equals("anonymous"));
        else {
            Users user = (Users)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            model.addAttribute("user_avatar", "/img/" + user.getAvatar().getData());
        }
        return "courses";
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String showAddCourses(Model model) {
        model.addAttribute("course", new Courses());
        if (getRole().equals("anonymous"));
        else {
            Users user = (Users)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            model.addAttribute("user_avatar", "/img/" + user.getAvatar().getData());
        }
        return path + "addcourse";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String showAddCourse(@Valid @ModelAttribute("course") Courses course,
                                BindingResult bindingResult,
                                Model model) {
        if (bindingResult.hasErrors()) {
            Map<String,String> errorsMap = ControllerUtils.getErrors(bindingResult);
            model.mergeAttributes(errorsMap);
            model.addAttribute("course", course);
            return path + "addcourse";
        }
        if (!course.getEnddate().after(course.getBegindate())){
            ObjectError error = new ObjectError("dateError", "Окончание не может быть раньше или в день начала курса");
            bindingResult.addError(error);
            return path + "addcourse";
        }
        else {
            course.setFreeseats(course.getSeats());
            coursesService.save(course);
        }
        model.addAttribute("courses", coursesService.getAllCourse());
        model.addAttribute("course", new Courses());
        model.addAttribute("user_role", getRole());
        if (getRole().equals("anonymous"));
        else {
            Users user = (Users)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            model.addAttribute("user_avatar", "/img/" + user.getAvatar().getData());
        }
        return "courses";
    }

    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public String showEditCourses(Model model,
                                  @ModelAttribute("course") Courses course) {
        course = coursesService.findCoursesById(course.getId());
        if (getRole().equals("anonymous"));
        else {
            Users user = (Users)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            model.addAttribute("user_avatar", "/img/" + user.getAvatar().getData());
        }
        model.addAttribute("course", course);
        return path + "editcourse";
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String showSaveCourse(@Valid @ModelAttribute("course") Courses course,
                                BindingResult bindingResult,
                                Model model) {
        if (bindingResult.hasErrors()) {
            Map<String,String> errorsMap = ControllerUtils.getErrors(bindingResult);
            model.mergeAttributes(errorsMap);
            model.addAttribute("course", course);
            return path + "editcourse";
        }
        if (!course.getEnddate().after(course.getBegindate())){
            ObjectError error = new ObjectError("dateError", "Окончание не может быть раньше или в день начала курса");
            bindingResult.addError(error);
            return path + "editcourse";
        }
        else {
            course.setFreeseats(course.getSeats() - (coursesService.findCoursesById(course.getId()).getSeats() - coursesService.findCoursesById(course.getId()).getFreeseats()));
            coursesService.save(course);
        }
        model.addAttribute("courses", coursesService.getAllCourse());
        model.addAttribute("course", new Courses());
        model.addAttribute("user_role", getRole());
        if (getRole().equals("anonymous"));
        else {
            Users user = (Users)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            model.addAttribute("user_avatar", "/img/" + user.getAvatar().getData());
        }
        return "courses";
    }

    @RequestMapping(value = "/remove", method = RequestMethod.POST)
    public String showRemoveCourses(Model model,
                                    @ModelAttribute("course") Courses course) {
        course = coursesService.findCoursesById(course.getId());
        coursesService.delete(course);
        Users user = (Users)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("user_avatar", "/img/" + user.getAvatar().getData());
        model.addAttribute("courses", coursesService.getAllCourse());
        model.addAttribute("course", new Courses());
        model.addAttribute("user_role", getRole());
        return "courses";
    }

    @RequestMapping(value = "/mentor", method = RequestMethod.GET)
    public String addMentorInCourses(Model model,
                                     @ModelAttribute("course") Courses course) {
        course = coursesService.findCoursesById(course.getId());

        Users user = (Users)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("user_avatar", "/img/" + user.getAvatar().getData());
        model.addAttribute("courses", coursesService.getAllCourse());
        model.addAttribute("course", new Courses());
        model.addAttribute("user_role", getRole());
        return "courses";
    }

    @RequestMapping(value = "/materials", method = RequestMethod.GET)
    public String showMaterials(Model model) {
        model.addAttribute("user_role", getRole());
        if (getRole().equals("anonymous"));
        else {
            Users user = (Users)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            model.addAttribute("user_avatar", "/img/" + user.getAvatar().getData());
        }
        return "materials";
    }
}