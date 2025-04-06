-- liquibase formatted sql

-- changeset RedSunRnD:1
CREATE INDEX student_idx_name ON students (name);

-- changeset RedSunRnD:2
CREATE INDEX faculty_idx_name_color ON faculties (name, color);