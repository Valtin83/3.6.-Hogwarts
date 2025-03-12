package com.example.Hogwarts.school.repository;

import com.example.Hogwarts.school.model.Avatar;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AvatarRepository extends JpaRepository<Avatar, Long> {

    Avatar findByStudentId(Long studentId);

    Page<Avatar> findAll(Pageable pageable);
}