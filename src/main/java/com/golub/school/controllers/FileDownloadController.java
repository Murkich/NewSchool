package com.golub.school.controllers;

import com.golub.school.entity.Avatar;
import com.golub.school.service.AvatarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;
import java.io.FileNotFoundException;

@Controller
public class FileDownloadController {
    @Autowired
    private AvatarService avatarService;

    @GetMapping("/downloadFile/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) throws FileNotFoundException {

        Avatar avatar = avatarService.getFile(Long.valueOf(fileName));

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(avatar.getType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + avatar.getName() + "\"")
                .body(new ByteArrayResource(avatar.getData()));
    }
}
