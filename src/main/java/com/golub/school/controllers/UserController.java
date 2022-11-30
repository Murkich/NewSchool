package com.golub.school.controllers;

import com.golub.school.entity.Courses;
import com.golub.school.entity.Users;
import com.golub.school.service.CoursesService;
import com.golub.school.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    UsersService usersService;

    @Autowired
    CoursesService coursesService;
    private static final String path = "usersTemplates/";

    @GetMapping()
    public String showUserPage() {
        return path + "user";
    }

    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    public String showProfile(Model model) {
        Users user = (Users)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("user", user);
        model.addAttribute("editOrSave", "save");
        return path + "profile";
    }

    @RequestMapping(value = "/profile/edit", method = RequestMethod.POST)
    public String editProfile(Model model, @ModelAttribute("user") Users user) {
        model.addAttribute("user", user);
        model.addAttribute("editOrSave", "edit");
        return path + "profile";
    }

    @RequestMapping(value = "profile/save", method = RequestMethod.POST)
    public String saveProfile(Model model,
                              @Valid @ModelAttribute("user") Users user,
                              BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String,String> errorsMap = ControllerUtils.getErrors(bindingResult);
            model.mergeAttributes(errorsMap);
            model.addAttribute("user", user);
            return "redirect:";
        }
        Users users = (Users)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        users.setName(user.getName());
        users.setSurname(user.getSurname());
        users.setTelephone(user.getTelephone());
        users.setUsername(user.getUsername());
        users.setCountry(user.getCountry());
        users.setBirthday(user.getBirthday());
        users.setEducation(user.getEducation());
        users.setWork(user.getWork());
        usersService.saveUser(users);
        model.addAttribute("user", users);
        model.addAttribute("editOrSave", "save");
        return path + "profile";
    }

    @RequestMapping(value = "/account", method = RequestMethod.GET)
    public String showAccount(Model model) {
        Users user = (Users)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        user.setPassword("************");
        model.addAttribute("user", user);
        model.addAttribute("editOrSave", "save");
        return path + "account";
    }

    @RequestMapping(value = "/account/edit", method = RequestMethod.POST)
    public String editAccount(Model model, @ModelAttribute("user") Users user) {
        model.addAttribute("email", user.getUsername());
        model.addAttribute("password", null);
        model.addAttribute("newPassword", null);
        model.addAttribute("passwordConfirm", null);
        model.addAttribute("editOrSave", "edit");
        return path + "account";
    }

    @RequestMapping(value = "/account/edit", method = RequestMethod.GET)
    public String editGetAccount(Model model, @ModelAttribute("user") Users user) {
        model.addAttribute("email", user.getUsername());
        model.addAttribute("password", null);
        model.addAttribute("newPassword", null);
        model.addAttribute("passwordConfirm", null);
        model.addAttribute("editOrSave", "edit");
        return path + "account";
    }

    @RequestMapping(value = "account/save", method = RequestMethod.POST)
    public String saveAccount(Model model,
                              HttpServletRequest request,
                              @Valid @ModelAttribute("user") Users user,
                              BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String,String> errorsMap = ControllerUtils.getErrors(bindingResult);
            model.mergeAttributes(errorsMap);
            model.addAttribute("user", user);
            return "redirect:edit";
        }
        Users users = (Users)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!user.getPassword().equals(users.getPassword())){
            ObjectError error = new ObjectError("passwordError", "Неверно введен текущий пароль");
            bindingResult.addError(error);
            return "redirect:edit";
        }
        if (!user.getNewPassword().equals(user.getPasswordConfirm())){
            ObjectError error = new ObjectError("passwordError", "Пароли не совпадают");
            bindingResult.addError(error);
            return "redirect:edit";
        }
        users.setUsername(user.getUsername());
        users.setPassword(user.getNewPassword());
        usersService.saveUser(users);
        users.setPassword("************");
        model.addAttribute("user", users);
        model.addAttribute("editOrSave", "save");
        return path + "account";
    }
    @RequestMapping(value = "/mycourses", method = RequestMethod.GET)
    public String showUsersCourses(Model model) {
        Users user = (Users)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("user", user);
        return path + "mycourses";
    }
    @RequestMapping(value = "/order", method = RequestMethod.GET)
    public String showUsersOrder(Model model,
                                 @ModelAttribute("course") Courses course) {
        course = coursesService.findCoursesById(course.getId());
        model.addAttribute("course", course);
        return path + "order";
    }
}