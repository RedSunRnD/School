package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    Logger logger = LoggerFactory.getLogger(FacultyService.class);

    public Faculty addFaculty(Faculty faculty) {
        logger.info("Was invoked method for add faculty");
        return facultyRepository.save(faculty);
    }

    public Faculty findFaculty(long id) {
        logger.info("Was invoked method for find faculty");
        return facultyRepository.findById(id).orElse(null);
    }

    public Faculty editFaculty(Faculty faculty) {
        logger.info("Was invoked method for edit faculty");
        return facultyRepository.save(faculty);
    }

    public void deleteFaculty(long id) {
        logger.info("Was invoked method for delete faculty");
        facultyRepository.deleteById(id);
    }

    public List<Faculty> getFacultyByColor(String color) {
        logger.info("Was invoked method for get faculty by color");
        return facultyRepository.findAllByColor(color);
    }

    public Collection<Faculty> getAllFaculty() {
        logger.info("Was invoked method for get faculty");
        return facultyRepository.findAll();
    }

    public Collection<Faculty> getFacultyByNameOrColor(String name, String color) {
        logger.info("Was invoked method for get faculty by name or color");
        return facultyRepository.findAllByNameIgnoreCaseOrColorIgnoreCase(name, color);
    }
}
