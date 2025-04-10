package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    Logger logger = LoggerFactory.getLogger(StudentService.class);

    public Student addStudent(Student student) {
        logger.info("Was invoked method for create student");
        return studentRepository.save(student);
    }

    public Student findStudent(long id) {
        logger.info("Was invoked method for find student");
        return studentRepository.findById(id)
                .orElse(null);
    }

    public Student editStudent(Student student) {
        logger.info("Was invoked method for update student");
        return studentRepository.save(student);
    }

    public void deleteStudent(long id) {
        logger.info("Was invoked method for delete student");
        studentRepository.deleteById(id);
    }

    public double getAverageAge() {
        return studentRepository.findAll()
                .stream()
                .mapToInt(Student::getAge)
                .average()
                .orElse(0);
    }

    public List<String> getNamesStartingWithA() {
        return studentRepository.findAll()
                .stream()
                .map(Student::getName)
                .filter(name -> name.startsWith("A"))
                .map(String::toUpperCase)
                .sorted()
                .collect(Collectors.toList());
    }

    public Collection<Student> getStudentsByAge(int age) {
        logger.info("Was invoked method for get students by age");
        return studentRepository.findAllByAge(age);
    }

    public Collection<Student> getAll() {
        logger.info("Was invoked method for get all students");
        return studentRepository.findAll();
    }

    public Collection<Student> getStudentsByAgeBetween(int minAge, int maxAge) {
        logger.info("Was invoked method for get students by age between");
        return studentRepository.findByAgeBetween(minAge, maxAge);
    }

    public Collection<Student> getStudentsByFaculty(long facultyId) {
        logger.info("Was invoked method for get students by faculty");
        return studentRepository.findByFacultyId(facultyId);
    }

    public Long calculateSumParallel() {
        return Stream.iterate(1L, a -> a + 1)
                .limit(1000000)
                .parallel()
                .reduce(0L, Long::sum);
    }

    public Long calculateSum() {
        return Stream.iterate(1L, a -> a + 1)
                .limit(1_000_000)
                .reduce(0L, Long::sum);
    }

    public Long sumMath() {
        int n = 1000000;
        return ((long) n * (n + 1) / 2);
    }

    public void printNamesParallel() {
        List<String> studentNames = studentRepository.findAll()
                .stream()
                .map(Student::getName)
                .limit(6)
                .toList();

        System.out.println(studentNames.get(0));
        System.out.println(studentNames.get(1));

        new Thread(() -> {
            System.out.println(studentNames.get(2));
            System.out.println(studentNames.get(3));
        }).start();

        new Thread(() -> {
            System.out.println(studentNames.get(4));
            System.out.println(studentNames.get(5));
        }).start();
    }

    public void printNamesSynchronized() {
        List<String> studentNames = studentRepository.findAll()
                .stream()
                .map(Student::getName)
                .limit(6)
                .toList();

        printNames(studentNames.get(0), studentNames.get(1));

        Thread thread1 = new Thread(() ->
                printNames(studentNames.get(2), studentNames.get(3))
        );

        Thread thread2 = new Thread(() ->
                printNames(studentNames.get(4), studentNames.get(5))
        );

        thread1.start();
        thread2.start();
    }

    private synchronized void printNames(String name1, String name2) {
        System.out.println(name1);
        System.out.println(name2);
    }
}

