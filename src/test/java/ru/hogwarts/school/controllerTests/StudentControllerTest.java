package ru.hogwarts.school.controllerTests;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StudentControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private StudentRepository studentRepository;

    private String getAddress() {
        return "http://localhost:" + port + "/student";
    }

    @BeforeEach
    public void setUp() {
        loadTestData();
    }

    @AfterEach
    public void resetDb() {
        studentRepository.findAll().forEach(student -> {
            if (student.getAvatar() != null) {
                student.setAvatar(null);
                studentRepository.save(student);
            }
        });
        studentRepository.deleteAll();
    }

    private void loadTestData() {
        Student student1 = new Student();
        student1.setName("TestName1");
        student1.setAge(20);

        Student student2 = new Student();
        student2.setName("TestName2");
        student2.setAge(25);

        studentRepository.save(student1);
        studentRepository.save(student2);
    }

    @Test
    public void createStudentTest() {
        Student student = new Student();
        student.setName("NewStudent");
        student.setAge(30);

        ResponseEntity<Student> response = testRestTemplate.postForEntity(
                getAddress(),
                student,
                Student.class
        );

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo(student.getName());
        assertThat(response.getBody().getAge()).isEqualTo(student.getAge());
    }

    @Test
    public void readStudentTest() {
        Student student = studentRepository.findAll().iterator().next();

        ResponseEntity<Student> response = testRestTemplate.getForEntity(
                getAddress() + "/" + student.getId(),
                Student.class
        );

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo(student.getName());
        assertThat(response.getBody().getAge()).isEqualTo(student.getAge());
    }

    @Test
    public void getAllStudentsTest() {
        ResponseEntity<List<Student>> response = testRestTemplate.exchange(
                getAddress(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Student>>() {}
        );

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().size()).isEqualTo(2);
        assertThat(response.getBody().get(0).getName()).isEqualTo("TestName1");
        assertThat(response.getBody().get(1).getName()).isEqualTo("TestName2");
    }

    @Test
    public void updateStudentTest() {
        Student student = studentRepository.findAll().iterator().next();
        student.setName("UpdatedName");
        student.setAge(25);

        ResponseEntity<Student> response = testRestTemplate.exchange(
                getAddress(),
                HttpMethod.PUT,
                new org.springframework.http.HttpEntity<>(student),
                Student.class
        );

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("UpdatedName");
        assertThat(response.getBody().getAge()).isEqualTo(25);
    }

    public void deleteStudentTest() {
        Student student = new Student();
        student.setName("Oleg");
        student.setAge(1);
        studentRepository.save(student);

        testRestTemplate.delete(getAddress() + "/" + student.getId());

        ResponseEntity<String> responseGet = testRestTemplate.getForEntity(
                getAddress() + "/" + student.getId(),
                String.class
        );

        // Ожидаем статус 404
        assertThat(responseGet.getStatusCode().value()).isEqualTo(404);
    }

    @Test
    public void filterByAgeTest() {
        ResponseEntity<List<Student>> response = testRestTemplate.exchange(
                getAddress() + "/age/20",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Student>>() {}
        );

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().size()).isEqualTo(1);
        assertThat(response.getBody().get(0).getName()).isEqualTo("TestName1");
    }

    @Test
    public void filterByAgeRangeTest() {
        ResponseEntity<List<Student>> response = testRestTemplate.exchange(
                getAddress() + "/age/range?ageAfter=20&ageBefore=30",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Student>>() {}
        );

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().size()).isEqualTo(2);
    }
}