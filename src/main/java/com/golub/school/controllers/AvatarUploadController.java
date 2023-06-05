package com.golub.school.controllers;

import com.golub.school.entity.Avatar;
import com.golub.school.entity.Users;
import com.golub.school.service.AvatarService;
import com.golub.school.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

@Controller
public class AvatarUploadController {
    @Autowired
    private AvatarService avatarService;
    @Autowired
    private UsersService usersService;
    public String getRole() {
        String role = SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString();
        if(Objects.equals(role, "[ROLE_ANONYMOUS]")) return  "anonymous";
        else if(Objects.equals(role, "[USER]")) return  "user";
        else if(Objects.equals(role, "[ADMIN]")) return  "admin";
        return "anonymous";
    }
    @PostMapping("/user/profile/edit/uploadImg")
    public String uploadFile(Model model,
                             @RequestParam("image") MultipartFile file) throws IOException {
        Users users = (Users) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (users.getAvatar().getId() == 1) {
            Avatar avatar = new Avatar();
            avatar.setName(file.getName());
            avatar.setType(file.getContentType());
            avatar.setData(file.getOriginalFilename());
            avatarService.save(avatar);
            users.setAvatar(avatar);
            usersService.saveUser(users);
        }
        else {
            Avatar avatar = users.getAvatar();
            avatar.setData(file.getOriginalFilename());
            avatar.setType(file.getContentType());
            avatarService.save(avatar);
            users.setAvatar(avatar);
            usersService.saveUser(users);
        }
        try {
            Path path = Paths.get("F:/School/src/main/resources/static/img/" + users.getAvatar().getData());
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
        model.addAttribute("user", users);
        model.addAttribute("user_role", getRole());
        model.addAttribute("user_avatar", "/img/" + users.getAvatar().getData());
        model.addAttribute("editOrSave", "edit");
        return "redirect:";
    }
}
