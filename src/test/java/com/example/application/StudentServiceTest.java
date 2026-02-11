package com.example.application;

import com.example.application.dto.StudentRequest;
import com.example.application.entity.Student;
import com.example.application.repository.StudentRepository;
import com.example.application.service.StudentService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Unit tests for StudentService")
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentService studentService;

    @Test
    @DisplayName("createStudent should map fields correctly and call repository.save")
    void createStudent_shouldSaveMappedStudent() {
        StudentRequest request = new StudentRequest();
        request.setFirstName("Misha");
        request.setLastName("Ivanov");
        request.setStudyGroup("A-101");
        request.setEmail("misha.kremloy@gmail.com");

        Student savedStudent = new Student();
        savedStudent.setFirstName("Misha");
        savedStudent.setLastName("Ivanov");
        savedStudent.setStudyGroup("A-101");
        savedStudent.setEmail("misha.kremloy@gmail.com");

        when(studentRepository.save(any(Student.class))).thenReturn(savedStudent);

        Student result = studentService.createStudent(request);

        assertEquals("Misha", result.getFirstName());
        assertEquals("Ivanov", result.getLastName());
        assertEquals("A-101", result.getStudyGroup());

        verify(studentRepository, times(1)).save(any(Student.class));
    }

    @Test
    @DisplayName("getStudentById should return student when found")
    void getStudentById_shouldReturnStudent() {
        Student student = new Student();
        student.setFirstName("Misha");

        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

        Student result = studentService.getStudentById(1L);

        assertEquals("Misha", result.getFirstName());
        verify(studentRepository).findById(1L);
    }

    @Test
    @DisplayName("getStudentById should throw EntityNotFoundException when student not found")
    void getStudentById_shouldThrowException() {
        when(studentRepository.findById(1L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> studentService.getStudentById(1L)
        );

        assertEquals("Not found student", exception.getMessage());
        verify(studentRepository).findById(1L);
    }

    @Test
    @DisplayName("createStudent should throw exception when request is null")
    void createStudent_nullRequest() {
        assertThrows(IllegalArgumentException.class,
                () -> studentService.createStudent(null));
    }

    @Test
    @DisplayName("createStudent should throw exception when fields are empty")
    void createStudent_emptyFields() {
        StudentRequest request = new StudentRequest();
        request.setFirstName(" ");
        request.setLastName(null);
        request.setStudyGroup("");

        assertThrows(IllegalArgumentException.class,
                () -> studentService.createStudent(request));
    }

    @Test
    @DisplayName("createStudent should trim values before saving")
    void createStudent_shouldTrimFields() {
        StudentRequest request = new StudentRequest();
        request.setFirstName("  Misha  ");
        request.setLastName("  Ivanov ");
        request.setStudyGroup(" A-101 ");
        request.setEmail("misha.kremloy@gmail.com");

        when(studentRepository.save(any()))
                .thenAnswer(inv -> inv.getArgument(0));

        Student result = studentService.createStudent(request);

        assertEquals("Misha", result.getFirstName());
        assertEquals("Ivanov", result.getLastName());
        assertEquals("A-101", result.getStudyGroup());
    }

    @Test
    @DisplayName("getStudentById should throw exception when id is null")
    void getStudentById_nullId() {
        assertThrows(IllegalArgumentException.class,
                () -> studentService.getStudentById(null));
    }

    @Test
    @DisplayName("getStudentById should throw exception when id is negative")
    void getStudentById_negativeId() {
        assertThrows(IllegalArgumentException.class,
                () -> studentService.getStudentById(-5L));
    }

    @Test
    @DisplayName("getStudentById should throw EntityNotFoundException when student not found")
    void getStudentById_notFound() {
        when(studentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> studentService.getStudentById(1L));
    }

    @Test
    @DisplayName("getStudentById should return student when found")
    void getStudentById_success() {
        Student student = new Student();
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

        Student result = studentService.getStudentById(1L);

        assertNotNull(result);
    }

}
