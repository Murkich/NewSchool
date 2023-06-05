package com.golub.school.controllers;

import com.golub.school.entity.*;
import com.golub.school.service.CardService;
import com.golub.school.service.CoursesService;
import com.golub.school.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import java.util.*;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    UsersService usersService;
    @Autowired
    CardService cardService;
    @Autowired
    CoursesService coursesService;
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;
    private static final String path = "usersTemplates/";
    private static final String path_admin = "adminsTemplates/";
    public String getRole() {
        String role = SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString();
        if(Objects.equals(role, "[ROLE_ANONYMOUS]")) return  "anonymous";
        else if(Objects.equals(role, "[USER]")) return  "user";
        else if(Objects.equals(role, "[ADMIN]")) return  "admin";
        return "anonymous";
    }

    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    public String showProfile(Model model) {
        Users user = (Users)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("user", user);
        model.addAttribute("user_avatar", "/img/" + user.getAvatar().getData());
        model.addAttribute("user_role", getRole());
        model.addAttribute("editOrSave", "save");
        return path + "profile";
    }

    @RequestMapping(value = "/profile/edit", method = RequestMethod.GET)
    public String editProfileGet(Model model) {
        Users users = (Users)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("user", users);
        model.addAttribute("user_role", getRole());
        model.addAttribute("user_avatar", "/img/" + users.getAvatar().getData());
        model.addAttribute("editOrSave", "edit");
        return path + "profile";
    }

    @RequestMapping(value = "/profile/edit", method = RequestMethod.POST)
    public String editProfile(Model model, @ModelAttribute("user") Users user) {
        Users users = (Users)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("user", user);
        model.addAttribute("user_role", getRole());
        model.addAttribute("user_avatar", "/img/" + users.getAvatar().getData());
        model.addAttribute("editOrSave", "edit");
        return path + "profile";
    }

    @RequestMapping(value = "profile/save", method = RequestMethod.POST)
    public String saveProfile(Model model,
                              @Valid @ModelAttribute("user") Users user,
                              BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Users users = (Users)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Map<String,String> errorsMap = ControllerUtils.getErrors(bindingResult);
            model.mergeAttributes(errorsMap);
            model.addAttribute("user", user);
            model.addAttribute("user_role", getRole());
            model.addAttribute("user_avatar", "/img/" + users.getAvatar().getData());
            model.addAttribute("editOrSave", "edit");
            return path + "profile";
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
        model.addAttribute("user_role", getRole());
        model.addAttribute("user_avatar", "/img/" + users.getAvatar().getData());
        model.addAttribute("editOrSave", "save");
        return path + "profile";
    }

    @RequestMapping(value = "/account", method = RequestMethod.GET)
    public String showAccount(Model model) {
        Users user = (Users)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        user.setPassword("************");
        model.addAttribute("user", user);
        model.addAttribute("user_role", getRole());
        model.addAttribute("user_avatar", "/img/" + user.getAvatar().getData());
        model.addAttribute("editOrSave", "save");
        return path + "account";
    }

    @RequestMapping(value = "/account/edit", method = RequestMethod.POST)
    public String editAccount(Model model, @ModelAttribute("user") Users user) {
        Users users = (Users)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("email", user.getUsername());
        model.addAttribute("password", null);
        model.addAttribute("newPassword", null);
        model.addAttribute("passwordConfirm", null);
        model.addAttribute("user_avatar", "/img/" + users.getAvatar().getData());
        model.addAttribute("user_role", getRole());
        model.addAttribute("editOrSave", "edit");
        return path + "account";
    }

    @RequestMapping(value = "/account/edit", method = RequestMethod.GET)
    public String editGetAccount(Model model, @ModelAttribute("user") Users user) {
        Users users = (Users)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("email", user.getUsername());
        model.addAttribute("password", null);
        model.addAttribute("newPassword", null);
        model.addAttribute("passwordConfirm", null);
        model.addAttribute("user_avatar", "/img/" + users.getAvatar().getData());
        model.addAttribute("user_role", getRole());
        model.addAttribute("editOrSave", "edit");
        return path + "account";
    }

    @RequestMapping(value = "/account/save", method = RequestMethod.POST)
    public String saveAccount(Model model,
                              @ModelAttribute("user") Users user,
                              BindingResult bindingResult) {
        Users users = (Users)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        users = usersService.findUserById(users.getId());
        if (user.getUsername().equals(users.getUsername()) && user.getPassword().equals("") && user.getPasswordConfirm().equals("") && user.getNewPassword().equals(""))
        {
            users.setPassword("************");
            model.addAttribute("user", users);
            model.addAttribute("user_avatar", "/img/" + users.getAvatar().getData());
            model.addAttribute("user_role", getRole());
            model.addAttribute("editOrSave", "save");
            return "redirect:";
        }
        if (!user.getUsername().equals(users.getUsername()) && user.getPassword().equals("") && user.getPasswordConfirm().equals("") && user.getNewPassword().equals(""))
        {
            users.setUsername(user.getUsername());
            usersService.saveUser(users);
            users.setPassword("************");
            model.addAttribute("user", users);
            model.addAttribute("user_avatar", "/img/" + users.getAvatar().getData());
            model.addAttribute("user_role", getRole());
            model.addAttribute("editOrSave", "save");
            return path + "account";
        }
        if (user.getPassword().equals("")){
            ObjectError error = new ObjectError("passwordError", "Введите старый пароль");
            bindingResult.addError(error);
            model.addAttribute("email", user.getUsername());
            model.addAttribute("password", null);
            model.addAttribute("newPassword", null);
            model.addAttribute("passwordConfirm", null);
            model.addAttribute("user_avatar", "/img/" + users.getAvatar().getData());
            model.addAttribute("user_role", getRole());
            model.addAttribute("editOrSave", "edit");
            return path + "account";
        }
        if (!bCryptPasswordEncoder.matches(user.getPassword(), users.getPassword())){
            ObjectError error = new ObjectError("passwordError", "Неверно введен старый пароль");
            bindingResult.addError(error);
            model.addAttribute("email", user.getUsername());
            model.addAttribute("password", null);
            model.addAttribute("newPassword", null);
            model.addAttribute("passwordConfirm", null);
            model.addAttribute("user_avatar", "/img/" + users.getAvatar().getData());
            model.addAttribute("user_role", getRole());
            model.addAttribute("editOrSave", "edit");
            return path + "account";
        }
        if (!user.getNewPassword().equals(user.getPasswordConfirm())){
            ObjectError error = new ObjectError("passwordError", "Пароли не совпадают");
            bindingResult.addError(error);
            model.addAttribute("email", user.getUsername());
            model.addAttribute("password", null);
            model.addAttribute("newPassword", null);
            model.addAttribute("passwordConfirm", null);
            model.addAttribute("user_avatar", "/img/" + users.getAvatar().getData());
            model.addAttribute("user_role", getRole());
            model.addAttribute("editOrSave", "edit");
            return path + "account";
        }
        Users CurUser = (Users)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        users.setUsername(user.getUsername());
        users.setPassword(bCryptPasswordEncoder.encode(user.getNewPassword()));
        usersService.saveUser(users);
        users.setPassword("************");
        model.addAttribute("user", users);
        model.addAttribute("user_avatar", "/img/" + CurUser.getAvatar().getData());
        model.addAttribute("user_role", getRole());
        model.addAttribute("editOrSave", "save");
        return path + "account";
    }

    @RequestMapping(value = "/mycourses", method = RequestMethod.GET)
    public String showUsersCourses(Model model) {
        Users user = (Users)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Collection<Courses> course = user.getCourses();
        model.addAttribute("user_avatar", "/img/" + user.getAvatar().getData());
        model.addAttribute("user_role", getRole());
        model.addAttribute("courses", course);
        model.addAttribute("user", user);
        return path + "mycourses";
    }

    @RequestMapping(value = "/order", method = RequestMethod.GET)
    public String showUsersOrder(Model model,
                                 @ModelAttribute("course") Courses course) {
        course = coursesService.findCoursesById(course.getId());
        Users user = (Users)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Card> card = cardService.findCardByUsersId(user.getId());
        model.addAttribute("user_avatar", "/img/" + user.getAvatar().getData());
        model.addAttribute("user", user);
        model.addAttribute("card", card == null? card : new Card());
        model.addAttribute("course", course);
        return path + "order";
    }

    @RequestMapping(value = "/order", method = RequestMethod.POST)
    public String showUserOrder(Model model,
                                @ModelAttribute("course") Courses course,
                                @ModelAttribute("card") Card card) {
        course = coursesService.findCoursesById(course.getId());
        //1857 5256 5962 1930
        Users user = (Users)SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        user.getCourses().add(course);
        course.setFreeseats(course.getFreeseats() - 1);
        usersService.saveUser(user);
        coursesService.save(course);

        model.addAttribute("user_avatar", "/img/" + user.getAvatar().getData());
        model.addAttribute("user", user);
        model.addAttribute("courses", user.getCourses());
        return path + "mycourses";
    }

    @RequestMapping(value = "/cancel", method = RequestMethod.POST)
    public String OrderCancel(Model model,
                              @ModelAttribute("course") Courses course) {
        course = coursesService.findCoursesById(course.getId());
        Users user = (Users)SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        user.getCourses().remove(course);
        course.setFreeseats(course.getFreeseats() + 1);
        usersService.saveUser(user);
        coursesService.save(course);

        model.addAttribute("user_avatar", "/img/" + user.getAvatar().getData());
        model.addAttribute("user", user);
        model.addAttribute("courses", user.getCourses());
        return "redirect:" + "mycourses";
    }

    @RequestMapping(value = "/lists", method = RequestMethod.GET)
    public String showListsUsers(Model model,
                                 @ModelAttribute("users") Users users) {
        Users user = (Users)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("users", usersService.getAllUsers());
        model.addAttribute("user_avatar", "/img/" + user.getAvatar().getData());
        model.addAttribute("user", new Users());
        model.addAttribute("user_me", user);
        return path_admin + "lists";
    }

    @RequestMapping(value = "/lists", method = RequestMethod.POST)
    public String showListUsers(Model model,
                                @ModelAttribute("users") Users users) {
        Users user = (Users)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("user_avatar", "/img/" + user.getAvatar().getData());
        model.addAttribute("users", usersService.getAllUsers());
        model.addAttribute("user", new Users());
        model.addAttribute("user_me", user);
        return path_admin + "lists";
    }

    @RequestMapping(value = "/block", method = RequestMethod.POST)
    public String showBlockUsers(Model model,
                                @ModelAttribute("users") Users users) {
        users = usersService.findUserById(users.getId());
        users.setActive(false);
        usersService.saveUser(users);
        Users user = (Users)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("user_avatar", "/img/" + user.getAvatar().getData());
        model.addAttribute("users", usersService.getAllUsers());
        model.addAttribute("user_me", user);
        return "redirect:" + "lists";
    }

    @RequestMapping(value = "/unblock", method = RequestMethod.POST)
    public String showUnblockUsers(Model model,
                                   @ModelAttribute("users") Users users) {
        users = usersService.findUserById(users.getId());
        users.setActive(true);
        usersService.saveUser(users);
        Users user = (Users)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("user_avatar", "/img/" + user.getAvatar().getData());
        model.addAttribute("users", usersService.getAllUsers());
        model.addAttribute("user_me", user);
        return "redirect:" + "lists";
    }

    @RequestMapping(value = "/admin-up", method = RequestMethod.POST)
    public String showAdminUpUsers(Model model,
                                   @ModelAttribute("users") Users users) {
        users = usersService.findUserById(users.getId());
        users.getRole().clear();
        users.getRole().add(Role.valueOf("ADMIN"));
        usersService.saveUser(users);
        Users user = (Users)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("user_avatar", "/img/" + user.getAvatar().getData());
        model.addAttribute("users", usersService.getAllUsers());
        model.addAttribute("user_me", user);
        return "redirect:" + "lists";
    }

    @RequestMapping(value = "/user-down", method = RequestMethod.POST)
    public String showUserDownUsers(Model model,
                                   @ModelAttribute("users") Users users) {
        users = usersService.findUserById(users.getId());
        users.getRole().clear();
        users.getRole().add(Role.valueOf("USER"));
        usersService.saveUser(users);
        Users user = (Users)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("user_avatar", "/img/" + user.getAvatar().getData());
        model.addAttribute("users", usersService.getAllUsers());
        model.addAttribute("user_me", user);
        return "redirect:" + "lists";
    }

    @RequestMapping(value = "/mentor-up", method = RequestMethod.POST)
    public String showMentorUsers(Model model,
                                  @ModelAttribute("users") Users users) {
        users = usersService.findUserById(users.getId());
        users.getRole().clear();
        users.getRole().add(Role.valueOf("MENTOR"));
        usersService.saveUser(users);
        Users user = (Users)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("user_avatar", "/img/" + user.getAvatar().getData());
        model.addAttribute("users", usersService.getAllUsers());
        model.addAttribute("user_me", user);
        return "redirect:" + "lists";
    }

    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public String showSearchUser(Model model,
                                 @ModelAttribute("user") Users users) {
        List<Users> usersList = usersService.findUsersBySurnameContaining(users.getSurname());
        Users user = (Users)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("user_avatar", "/img/" + user.getAvatar().getData());
        model.addAttribute("users", usersList);
        model.addAttribute("user_me", user);
        return path_admin + "lists";
    }

    @RequestMapping(value = "/roleu", method = RequestMethod.GET)
    public String showUserRole(Model model) {
        List<Users> usersList = usersService.findUsersByRole("USER");
        Users user = (Users)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("user_avatar", "/img/" + user.getAvatar().getData());
        model.addAttribute("users", usersList);
        model.addAttribute("user", new Users());
        model.addAttribute("user_me", user);
        return path_admin + "lists";
    }

    @RequestMapping(value = "/rolea", method = RequestMethod.GET)
    public String showAdminRole(Model model) {
        List<Users> usersList = usersService.findUsersByRole("ADMIN");
        Users user = (Users)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("user_avatar", "/img/" + user.getAvatar().getData());
        model.addAttribute("users", usersList);
        model.addAttribute("user", new Users());
        model.addAttribute("user_me", user);
        return path_admin + "lists";
    }

    @RequestMapping(value = "/rolem", method = RequestMethod.GET)
    public String showMentorRole(Model model) {
        List<Users> usersList = usersService.findUsersByRole("MENTOR");
        Users user = (Users)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("user_avatar", "/img/" + user.getAvatar().getData());
        model.addAttribute("users", usersList);
        model.addAttribute("user", new Users());
        model.addAttribute("user_me", user);
        return path_admin + "lists";
    }
}