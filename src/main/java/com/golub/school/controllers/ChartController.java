package com.golub.school.controllers;

import com.golub.school.entity.Courses;
import com.golub.school.entity.Users;
import com.golub.school.service.CoursesService;
import com.golub.school.service.UsersService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Controller
@RequestMapping("/chart")
public class ChartController {

    private final CoursesService coursesService;

    private final UsersService usersService;

    public ChartController(UsersService usersService, CoursesService coursesService) {
        this.coursesService = coursesService;
        this.usersService = usersService;
    }

    public String getRole() {
        String role = SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString();
        if(Objects.equals(role, "[ROLE_ANONYMOUS]")) return  "anonymous";
        else if(Objects.equals(role, "[USER]")) return  "user";
        else if(Objects.equals(role, "[ADMIN]")) return  "admin";
        else if(Objects.equals(role, "[MENTOR]")) return  "mentor";
        return "anonymous";
    }

    @GetMapping()
    public String showChart(Model model) {
        List<Courses> courses = coursesService.getAllCourse();
        Map<String, Integer> languageCountMap = new HashMap<>();

        for (Courses course : courses) {
            String language = course.getLanguage();
            languageCountMap.put(language, languageCountMap.getOrDefault(language, 0) + 1);
        }

        List<Users> users = usersService.getAllUsers();
        Map<String, Integer> usersCountMap = new HashMap<>();

        for (Users user : users) {
            String role = user.getRole().toString();
            switch (role) {
                case "[USER]":
                    role = "user";
                    break;
                case "[ADMIN]":
                    role = "admin";
                    break;
                case "[MENTOR]":
                    role = "mentor";
                    break;
            }
            usersCountMap.put(role, usersCountMap.getOrDefault(role, 0) + 1);
        }

        if (getRole().equals("anonymous"));
        else {
            Users user = (Users) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            model.addAttribute("user_avatar", "/img/" + user.getAvatar().getData());
        }
        model.addAttribute("user_role", getRole());
        model.addAttribute("languageCountMap", languageCountMap);
        model.addAttribute("usersCountMap", usersCountMap);
        return "chart";
    }
}
