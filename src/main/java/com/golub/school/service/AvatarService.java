package com.golub.school.service;

import com.golub.school.entity.Avatar;
import com.golub.school.repository.AvatarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

@Service
public class AvatarService {
    @Autowired
    private AvatarRepository avatarRepository;
    public void save(Avatar avatar) { avatarRepository.save(avatar); }
    public List<Avatar> getAllAvatar()
    {
        return avatarRepository.findAll();
    }
    public Avatar storeFile(MultipartFile file) throws IOException {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        Avatar dbFile = new Avatar(fileName, file.getContentType(), fileName);

        return avatarRepository.save(dbFile);
    }
    public Avatar getFile(Long fileId) throws FileNotFoundException {
        return avatarRepository.findById(fileId)
                .orElseThrow(() -> new FileNotFoundException("Файл не найден " + fileId));
    }
}
