package com.golub.school.controllers;

import com.golub.school.entity.Role;
import com.golub.school.entity.Users;
import com.golub.school.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;
import java.util.Map;

@Controller
public class RegistrationController {
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private UsersService userService;

    @GetMapping("/reg")
    public String registration(Model model) {
        model.addAttribute("user", new Users());
        return "reg";
    }

    @PostMapping("/reg")
    public String regUser(@Valid @ModelAttribute("user") Users user,
                          BindingResult bindingResult,
                          Model model) {
        if (bindingResult.hasErrors()) {
            Map<String,String> errorsMap = ControllerUtils.getErrors(bindingResult);
            model.mergeAttributes(errorsMap);
            model.addAttribute("user", user);
            return "reg";
        }
        if (!user.getPassword().equals(user.getPasswordConfirm())){
            ObjectError error = new ObjectError("passwordError", "Пароли не совпадают");
            bindingResult.addError(error);
            return "reg";
        }
        if(userService.findByUsername(user.getUsername())) {
            ObjectError error = new ObjectError("emailError", "Пользователь с таким email уже зарегистрирован");
            bindingResult.addError(error);
            return "reg";
        }
        else {
            user.setActive(true);
            user.setRole(Collections.singleton(Role.valueOf("USER")));
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            userService.saveUser(user);
        }
        return "signin";
    }
}
