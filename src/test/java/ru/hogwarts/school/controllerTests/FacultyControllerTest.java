package ru.hogwarts.school.controllerTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FacultyControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private FacultyRepository facultyRepository;

    @Autowired
    private StudentRepository studentRepository;

    private String baseUrl;

    @BeforeEach
    public void setUp() {
        baseUrl = "http://localhost:" + port + "/faculty";
        studentRepository.deleteAll(); // Очищаем таблицу студентов перед факультетами
        facultyRepository.deleteAll(); // Очищаем таблицу факультетов
    }

    @Test
    public void testAddFaculty() {
        Faculty faculty = new Faculty();
        faculty.setName("Gryffindor");
        faculty.setColor("Red");

        ResponseEntity<Faculty> response = restTemplate.postForEntity(baseUrl, faculty, Faculty.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Gryffindor", response.getBody().getName());
        assertEquals("Red", response.getBody().getColor());
    }

    @Test
    public void testFindFaculty() {
        Faculty faculty = new Faculty();
        faculty.setName("Slytherin");
        faculty.setColor("Green");
        faculty = facultyRepository.save(faculty);

        ResponseEntity<Faculty> response = restTemplate.getForEntity(baseUrl + "/" + faculty.getId(), Faculty.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Slytherin", response.getBody().getName());
        assertEquals("Green", response.getBody().getColor());
    }

    @Test
    public void testEditFaculty() {
        Faculty faculty = new Faculty();
        faculty.setName("Hufflepuff");
        faculty.setColor("Yellow");
        faculty = facultyRepository.save(faculty);

        faculty.setColor("Blue");
        restTemplate.put(baseUrl, faculty);

        ResponseEntity<Faculty> response = restTemplate.getForEntity(baseUrl + "/" + faculty.getId(), Faculty.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Blue", response.getBody().getColor());
    }

    @Test
    public void testDeleteFaculty() {
        Faculty faculty = new Faculty();
        faculty.setName("Ravenclaw");
        faculty.setColor("Blue");
        faculty = facultyRepository.save(faculty);

        restTemplate.delete(baseUrl + "/" + faculty.getId());

        ResponseEntity<Faculty> response = restTemplate.getForEntity(baseUrl + "/" + faculty.getId(), Faculty.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testGetFacultyByColor() {
        Faculty faculty1 = new Faculty();
        faculty1.setName("Gryffindor");
        faculty1.setColor("Red");
        facultyRepository.save(faculty1);

        Faculty faculty2 = new Faculty();
        faculty2.setName("Slytherin");
        faculty2.setColor("Green");
        facultyRepository.save(faculty2);

        ResponseEntity<Collection> response = restTemplate.getForEntity(baseUrl + "/color/Red", Collection.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
    }

    @Test
    public void testGetAllFaculty() {
        Faculty faculty1 = new Faculty();
        faculty1.setName("Gryffindor");
        faculty1.setColor("Red");
        facultyRepository.save(faculty1);

        Faculty faculty2 = new Faculty();
        faculty2.setName("Slytherin");
        faculty2.setColor("Green");
        facultyRepository.save(faculty2);

        ResponseEntity<Collection> response = restTemplate.getForEntity(baseUrl, Collection.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
    }

    @Test
    public void testGetStudentsByFaculty() {
        Faculty faculty = new Faculty();
        faculty.setName("Gryffindor");
        faculty.setColor("Red");
        faculty = facultyRepository.save(faculty);

        Student student1 = new Student();
        student1.setName("Harry Potter");
        student1.setFaculty(faculty);
        studentRepository.save(student1);

        Student student2 = new Student();
        student2.setName("Hermione Granger");
        student2.setFaculty(faculty);
        studentRepository.save(student2);

        ResponseEntity<Collection> response = restTemplate.getForEntity(baseUrl + "/" + faculty.getId() + "/students", Collection.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
    }
}