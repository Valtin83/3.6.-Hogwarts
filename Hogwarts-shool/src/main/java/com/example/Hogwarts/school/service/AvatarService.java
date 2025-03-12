package com.example.Hogwarts.school.service;

import com.example.Hogwarts.school.model.Avatar;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.multipart.MultipartFile;

import java.awt.print.Pageable;
import java.io.IOException;

public interface AvatarService {

    void uploadAvatar(Long studentId, MultipartFile file) throws IOException;

    Avatar findAvatar(Long id);

    Page<Avatar> getAvatars(PageRequest pageable);

    Page<Avatar> getAvatars(Pageable pageable);
}

