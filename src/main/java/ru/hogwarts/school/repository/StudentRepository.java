package ru.hogwarts.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ru.hogwarts.school.model.Student;

import java.util.Collection;
import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {
    Collection<Student> findAllByAge(int age);

    Collection<Student> findByAgeBetween(int ageAfter, int ageBefore);

    Collection<Student> findByFacultyId(long facultyId);

    @Query(value = "SELECT COUNT(*) AS count FROM student", nativeQuery = true)
    Integer getCountOfStudents();

    @Query(value = "SELECT AVG(age) as avg FROM student", nativeQuery = true)
    double getAverageAge();

    @Query(value = "SELECT * FROM student ORDER BY id DESC LIMIT 5", nativeQuery = true)
    List<Student> findLastFiveStudents();
}
