package com.example.Hogwarts.school.service;

import com.example.Hogwarts.school.exeption.StudentNotFoundException;
import com.example.Hogwarts.school.model.Avatar;
import com.example.Hogwarts.school.model.Student;
import com.example.Hogwarts.school.repository.AvatarRepository;
import com.example.Hogwarts.school.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.print.Pageable;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

@Service
@Transactional
public class AvatarServiceImpl implements AvatarService {

    @Value("${avatars.dir.path}")
    private String avatarsDir;

    @Autowired
    private final StudentRepository studentRepository;
    private final AvatarRepository avatarRepository;

    public AvatarServiceImpl(StudentRepository studentRepository, AvatarRepository avatarRepository) {
        this.studentRepository = studentRepository;
        this.avatarRepository = avatarRepository;
    }

    @Override
    public void uploadAvatar(Long studentId, MultipartFile file) throws IOException {
        Student student = getStudent(studentId);

        Path filePath = Path.of(avatarsDir, studentId + "." + getExtension(Objects.requireNonNull(file.getOriginalFilename())));
        Files.createDirectories(filePath.getParent());
        Files.deleteIfExists(filePath);

        try (InputStream is = file.getInputStream();
             OutputStream os = Files.newOutputStream(filePath, CREATE_NEW);
             BufferedInputStream bis = new BufferedInputStream(is, 1024);
             BufferedOutputStream bos = new BufferedOutputStream(os, 1024);
        ) {
            bis.transferTo(bos);
        }

        Avatar avatar = findAvatar(studentId);
        avatar.setStudent(student);
        avatar.setFilePath(filePath.toString());
        avatar.setFileSize(file.getSize());
        avatar.setMediaType(file.getContentType());
        avatar.setPreview(file.getBytes());

        avatarRepository.save(avatar);
    }

    private byte[] generateImagePreview(Path filePath) throws IOException {
        try (InputStream is = Files.newInputStream(filePath);
             BufferedInputStream bis = new BufferedInputStream(is, 1024);
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            BufferedImage image = ImageIO.read(bis);

            int height = image.getHeight() / (image.getWidth() / 100);
            BufferedImage preview = new BufferedImage(100, height, image.getType());
            Graphics2D graphics = preview.createGraphics();
            graphics.drawImage(image,0,0,100, height, null);
            graphics.dispose();

            ImageIO.write(preview, getExtension(filePath.getFileName().toString()), baos);
            return baos.toByteArray();
        }
    }

    @Override
    public Avatar findAvatar(Long id) {
        return avatarRepository.findByStudentId(id);
    }

    @Override
    public Page<Avatar> getAvatars(Pageable pageable) {
        return avatarRepository.findAll((org.springframework.data.domain.Pageable) pageable);
    }

    public Student getStudent(Long studentId) {
        return studentRepository.findById(studentId)
                .orElseThrow(() -> new StudentNotFoundException("Студент с ID " + studentId + " не найден"));
    }

    private String getExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

}

