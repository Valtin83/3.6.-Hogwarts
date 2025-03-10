package com.example.Hogwarts.school.service;

import com.example.Hogwarts.school.model.Faculty;
import com.example.Hogwarts.school.model.Student;

import java.util.List;

public interface StudentService {

    Student createStudent(Student student);

    Student getStudent(Long id);

    Student updateStudent(Student student);

    void removeStudent(Long id);

    Faculty getStudentFaculty(Long studentId);

    List<Student> getStudentsByAgeRange(int min, int max);

}
