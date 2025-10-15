INSERT INTO course (code, name, degree_program) VALUES
('CS101', 'Introduction to Computer Science', 'MS Computer Science'),
('CS102', 'Data Structures', 'MS Computer Science'),
('CS201', 'Algorithms', 'MS Computer Science'),
('CS301', 'Machine Learning', 'MS Computer Science'),
('CS401', 'Deep Learning', 'MS Computer Science');

UPDATE course SET prerequisite_id = (SELECT id FROM course WHERE code = 'CS102') WHERE code = 'CS201';
UPDATE course SET prerequisite_id = (SELECT id FROM course WHERE code = 'CS301') WHERE code = 'CS401';



INSERT INTO student (email, password, name, degree, year, gpa)
VALUES ('antara@example.com', 'mypassword', 'Antara Bhavsar', 'MS Computer Science', 2, 3.9);

INSERT INTO student_course (student_id, course_id)
SELECT s.id, c.id FROM student s, course c
WHERE s.email = 'antara@example.com' AND c.code = 'CS101';

INSERT INTO student_course (student_id, course_id)
SELECT s.id, c.id FROM student s, course c
WHERE s.email = 'antara@example.com' AND c.code = 'CS102';

INSERT INTO student_course (student_id, course_id)
SELECT s.id, c.id FROM student s, course c
WHERE s.email = 'antara@example.com' AND c.code = 'CS201';