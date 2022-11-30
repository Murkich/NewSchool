package com.golub.school.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class SchoolController {
    @GetMapping()
    public String redirectSchool() {
        return "start";
    }
}