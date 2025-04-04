package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.repository.FacultyRepository;

import java.util.Collection;
import java.util.List;
@Service
public class FacultyService {

    private final FacultyRepository facultyRepository;

    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    public Faculty addFaculty(Faculty faculty) {
        return facultyRepository.save(faculty);
    }

    public Faculty findFaculty(long id) {
        return facultyRepository.findById(id).orElse(null);
    }

    public Faculty editFaculty(Faculty faculty) {
        return facultyRepository.save(faculty);
    }

    public void deleteFaculty(long id) {
        facultyRepository.deleteById(id);
    }

    public List<Faculty> getFacultyByColor(String color) {
        return facultyRepository.findAllByColor(color);
    }

    public Collection<Faculty> getAllFaculty() {
        return facultyRepository.findAll();
    }

    public Collection<Faculty> getFacultyByNameOrColor(String name, String color) {
        return facultyRepository.findAllByNameIgnoreCaseOrColorIgnoreCase(name, color);
    }
}
