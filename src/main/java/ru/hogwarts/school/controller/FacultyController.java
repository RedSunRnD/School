package ru.hogwarts.school.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.service.FacultyService;
import ru.hogwarts.school.service.StudentService;

import java.util.Collection;
import java.util.Comparator;

@RestController
@RequestMapping("/faculty")
public class FacultyController {
    private final FacultyService facultyService;
    private final StudentService studentService;
    private final FacultyRepository facultyRepository;

    public FacultyController(FacultyService facultyService, StudentService studentService, FacultyRepository facultyRepository) {
        this.facultyService = facultyService;
        this.studentService = studentService;
        this.facultyRepository = facultyRepository;
    }

    @PostMapping
    public Faculty addFaculty(@RequestBody Faculty faculty) {
        return facultyService.addFaculty(faculty);
    }

    @GetMapping("{id}")
    public ResponseEntity<Faculty> findFaculty(@PathVariable long id) {
        Faculty faculty = facultyService.findFaculty(id);
        if (faculty == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(faculty);
    }

    @GetMapping("/longest-name")
    public String findLongestFacultyName() {
        return facultyService.findLongestFacultyName();
    }

    @PutMapping
    public ResponseEntity<Faculty> editFaculty(@RequestBody Faculty faculty) {
        Faculty foundFaculty = facultyService.editFaculty(faculty);
        if (foundFaculty == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(foundFaculty);
    }

    @DeleteMapping("{id}")
    public void deleteFaculty(@PathVariable long id) {
        facultyService.deleteFaculty(id);
    }

    @GetMapping("/color/{color}")
    public Collection<Faculty> getFacultyByColor(@PathVariable String color) {
        return facultyService.getFacultyByColor(color);
    }

    @GetMapping
    public Collection<Faculty> getAllFaculty(@RequestParam(required = false) String name,
                                             @RequestParam(required = false) String color) {
        if (name != null || color != null) {
            return facultyService.getFacultyByNameOrColor(name, color);
        }
        return facultyService.getAllFaculty();
    }

    @GetMapping("{id}/students")
    public ResponseEntity<Collection<Student>> getStudentsByFaculty(@PathVariable long id) {
        Faculty faculty = facultyService.findFaculty(id);
        if (faculty == null) {
            return ResponseEntity.notFound().build();
        }
        Collection<Student> students = studentService.getStudentsByFaculty(id);
        return ResponseEntity.ok(students);
    }
}
