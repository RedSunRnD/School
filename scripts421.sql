ALTER TABLE students ADD CONSTRAINT age_constraint CHECK ( age >= 16 );
ALTER TABLE students ADD CONSTRAINT unique_name UNIQUE (name);
ALTER TABLE students ALTER COLUMN name SET NOT NULL ;
ALTER TABLE faculties ADD CONSTRAINT unique_name_color UNIQUE (name, color);
ALTER TABLE students ALTER COLUMN age SET DEFAULT 20;