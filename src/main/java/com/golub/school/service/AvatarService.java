package com.golub.school.service;

import com.golub.school.entity.Avatar;
import com.golub.school.repository.AvatarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;

@Service
public class AvatarService {
    @Autowired
    private AvatarRepository avatarRepository;
    public Avatar storeFile(MultipartFile file) throws IOException {

        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        Avatar dbFile = new Avatar(fileName, file.getContentType(), file.getBytes());

        return avatarRepository.save(dbFile);
    }
    public Avatar getFile(Long fileId) throws FileNotFoundException {
        return avatarRepository.findById(fileId)
                .orElseThrow(() -> new FileNotFoundException("Файйл не найден" + fileId));
    }
}
