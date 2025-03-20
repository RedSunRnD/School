package ru.hogwarts.school.controllerTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.hogwarts.school.controller.StudentController;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.AvatarService;
import ru.hogwarts.school.service.StudentService;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StudentController.class)
public class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentService studentService;

    @MockBean
    private AvatarService avatarService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testGetAllStudents() throws Exception {
        when(studentService.getAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/student"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    public void testFindStudent() throws Exception {
        Student student = new Student();
        student.setId(1L);
        student.setName("Harry Potter");
        student.setAge(15);

        when(studentService.findStudent(1L)).thenReturn(student);

        mockMvc.perform(get("/student/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Harry Potter"))
                .andExpect(jsonPath("$.age").value(15));
    }

    @Test
    public void testFindStudentNotFound() throws Exception {
        when(studentService.findStudent(1L)).thenReturn(null);

        mockMvc.perform(get("/student/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testAddStudent() throws Exception {
        Student student = new Student();
        student.setName("Hermione Granger");
        student.setAge(15);

        when(studentService.addStudent(any(Student.class))).thenReturn(student);

        mockMvc.perform(post("/student")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(student)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Hermione Granger"))
                .andExpect(jsonPath("$.age").value(15));
    }

    @Test
    public void testEditStudent() throws Exception {
        Student student = new Student();
        student.setId(1L);
        student.setName("Ron Weasley");
        student.setAge(16);

        when(studentService.editStudent(any(Student.class))).thenReturn(student);

        mockMvc.perform(put("/student")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(student)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Ron Weasley"))
                .andExpect(jsonPath("$.age").value(16));
    }

    @Test
    public void testEditStudentNotFound() throws Exception {
        Student student = new Student();
        student.setId(1L);
        student.setName("Draco Malfoy");
        student.setAge(16);

        when(studentService.editStudent(any(Student.class))).thenReturn(null);

        mockMvc.perform(put("/student")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(student)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testDeleteStudent() throws Exception {
        mockMvc.perform(delete("/student/1"))
                .andExpect(status().isOk());
    }

    @Test
    public void testFindStudentsByAge() throws Exception {
        Student student = new Student();
        student.setId(1L);
        student.setName("Neville Longbottom");
        student.setAge(15);

        when(studentService.getStudentsByAge(15)).thenReturn(List.of(student));

        mockMvc.perform(get("/student/age/15"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Neville Longbottom"))
                .andExpect(jsonPath("$[0].age").value(15));
    }

    @Test
    public void testFindStudentsByAgeBetween() throws Exception {
        Student student = new Student();
        student.setId(1L);
        student.setName("Luna Lovegood");
        student.setAge(14);

        when(studentService.getStudentsByAgeBetween(14, 16)).thenReturn(List.of(student));

        mockMvc.perform(get("/student/age/range?ageAfter=14&ageBefore=16"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Luna Lovegood"))
                .andExpect(jsonPath("$[0].age").value(14));
    }

    @Test
    public void testGetFacultyOfStudent() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setId(1L);
        faculty.setName("Gryffindor");
        faculty.setColor("Red");

        Student student = new Student();
        student.setId(1L);
        student.setFaculty(faculty);

        when(studentService.findStudent(1L)).thenReturn(student);

        mockMvc.perform(get("/student/1/faculty"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Gryffindor"))
                .andExpect(jsonPath("$.color").value("Red"));
    }

    @Test
    public void testGetFacultyOfStudentNotFound() throws Exception {
        when(studentService.findStudent(1L)).thenReturn(null);

        mockMvc.perform(get("/student/1/faculty"))
                .andExpect(status().isNotFound());
    }
}