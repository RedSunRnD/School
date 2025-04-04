SELECT students.name AS student_name,
       students.age,
       faculties.name AS faculty_name
FROM students
         JOIN faculties ON students.faculty_id = faculties.id;

SELECT students.name AS student_name,
       students.age,
       faculties.name AS faculty_name
FROM students
         JOIN faculties ON students.faculty_id = faculties.id;