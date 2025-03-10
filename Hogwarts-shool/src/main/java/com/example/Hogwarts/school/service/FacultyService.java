package com.example.Hogwarts.school.service;

import com.example.Hogwarts.school.model.Faculty;
import com.example.Hogwarts.school.model.Student;

import java.util.List;

public interface FacultyService {

    Faculty createFaculty(Faculty faculty);

    Faculty getFaculty(Long id);

    Faculty updateFaculty(Faculty faculty);

    void removeFaculty(Long id);

    List<Student> getStudentsByFaculty(Long facultyId);

    List<Faculty> searchByColorOrName(String color, String name);
}
