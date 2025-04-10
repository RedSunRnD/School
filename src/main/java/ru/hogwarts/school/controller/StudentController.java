package ru.hogwarts.school.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.service.AvatarService;
import ru.hogwarts.school.service.StudentService;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/student")
public class StudentController {
    private final StudentService studentService;
    private final StudentRepository studentRepository;

    public StudentController(StudentService studentService, AvatarService avatarService, StudentRepository studentRepository) {
        this.studentService = studentService;
        this.studentRepository = studentRepository;
    }

    @GetMapping
    public Collection<Student> getAllStudents() {
        return studentService.getAll();
    }

    @GetMapping("{id}")
    public ResponseEntity<Student> findStudent(@PathVariable long id) {
        Student student = studentService.findStudent(id);
        if (student == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(student);
    }

    @GetMapping("/count")
    public long getStudentCount() {
        return studentRepository.getCountOfStudents();
    }

    @GetMapping("/average-age")
    public double getAverageAge() {
        return studentRepository.getAverageAge();
    }

    @GetMapping("/last-five")
    public Collection<Student> getLastFiveStudents() {
        return studentRepository.findLastFiveStudents();
    }

    @GetMapping("/names-starting-with-a")
    public List<String> getNamesStartingWithA() {
        return studentService.getNamesStartingWithA();
    }

    @GetMapping("/get-average-age")
    public double getAverageAgeOfStudents() {
        return studentService.getAverageAge();
    }

    @GetMapping("/get-sum")
    public Long sum() {
        return studentService.calculateSum();
    }

    @GetMapping("/sum-fastest")
    public Long sumFastest() {
        return studentService.sumMath();
    }

    @GetMapping("/print-parallel")
    public ResponseEntity<String> printStudentNamesParallel() {
        studentService.printNamesParallel();
        return ResponseEntity.ok("Check console output");
    }

    @GetMapping("/print-synchronized")
    public ResponseEntity<String> printStudentNamesSynchronized() {
        studentService.printNamesSynchronized();
        return ResponseEntity.ok("Check console output");
    }


    @PostMapping
    public Student addStudent(@RequestBody Student student) {
        return studentService.addStudent(student);
    }

    @PutMapping
    public ResponseEntity<Student> editStudent(@RequestBody Student student) {
        Student foundStudent = studentService.editStudent(student);
        if (foundStudent == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(foundStudent);
    }

    @DeleteMapping("/{id}")
    public void deleteStudent(@PathVariable long id) {
        studentService.deleteStudent(id);
    }

    @GetMapping("/age/{age}")
    public Collection<Student> findStudentsByAge(@PathVariable int age) {
        return studentService.getStudentsByAge(age);
    }

    @GetMapping("/age/range")
    public Collection<Student> findStudentsByAgeBetween(@RequestParam(required = false) int ageAfter,
                                                        @RequestParam(required = false) int ageBefore) {
        return studentService.getStudentsByAgeBetween(ageAfter, ageBefore);
    }

    @GetMapping("/{id}/faculty")
    public ResponseEntity<Faculty> getFacultyOfStudent(@PathVariable long id) {
        Student student = studentService.findStudent(id);
        if (student == null || student.getFaculty() == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(student.getFaculty());
    }
}
