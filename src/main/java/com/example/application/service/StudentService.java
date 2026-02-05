package com.example.application.service;

import com.example.application.dto.StudentRequest;
import com.example.application.entity.Student;
import com.example.application.repository.StudentRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;

    @Transactional
    public Student createStudent(StudentRequest request) {

        if (request == null) {
            throw new IllegalArgumentException("StudentRequest must not be null");
        }

        if (isBlank(request.getFirstName())
                || isBlank(request.getLastName())
                || isBlank(request.getStudyGroup())) {
            throw new IllegalArgumentException("Student fields must not be empty");
        }

        Student student = new Student();
        student.setFirstName(request.getFirstName().trim());
        student.setLastName(request.getLastName().trim());
        student.setStudyGroup(request.getStudyGroup().trim());

        return studentRepository.save(student);
    }

    public Student getStudentById(Long id) {

        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Id must be positive and not null");
        }

        return studentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Not found student"));
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}

