package com.golub.school.controllers;

import com.golub.school.entity.Avatar;
import com.golub.school.entity.Response;
import com.golub.school.service.AvatarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;

@Controller
public class AvatarUploadController {
    @Autowired
    private AvatarService avatarService;

    @PostMapping("/user/profile/uploadImg")
    public Response uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        Avatar fileName = avatarService.storeFile(file);

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/downloadFile/")
                .path(fileName.getName())
                .toUriString();

        return new Response(fileName.getName(), fileDownloadUri, file.getContentType(), file.getSize());
    }
}
