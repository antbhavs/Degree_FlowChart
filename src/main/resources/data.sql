INSERT OR IGNORE INTO course (code, name, degree_program) VALUES
 ('CS101', 'Introduction to Computer Science', 'MS Computer Science'),
 ('CS102', 'Data Structures', 'MS Computer Science'),
 ('CS103', 'Discrete Mathematics', 'MS Computer Science'),
 ('CS104', 'Computer Architecture', 'MS Computer Science'),
 ('CS105', 'Programming Paradigms', 'MS Computer Science'),
 ('CS106', 'Computer Graphics', 'MS Computer Science'),
  ('CS107', 'Human-Computer Interaction', 'MS Computer Science'),
  ('CS108', 'Introduction to Quantum Computing', 'MS Computer Science'),

 ('CS201', 'Algorithms', 'MS Computer Science'),
 ('CS202', 'Database Systems', 'MS Computer Science'),
 ('CS203', 'Operating Systems', 'MS Computer Science'),
 ('CS204', 'Computer Networks', 'MS Computer Science'),
 ('CS205', 'Software Engineering', 'MS Computer Science'),

 ('CS301', 'Machine Learning', 'MS Computer Science'),
 ('CS302', 'Artificial Intelligence', 'MS Computer Science'),
 ('CS303', 'Compiler Design', 'MS Computer Science'),
 ('CS304', 'Distributed Systems', 'MS Computer Science'),
 ('CS305', 'Cloud Computing', 'MS Computer Science'),

 ('CS401', 'Deep Learning', 'MS Computer Science'),
 ('CS402', 'Advanced AI Applications', 'MS Computer Science'),
 ('CS403', 'Big Data Analytics', 'MS Computer Science'),
 ('CS404', 'Cybersecurity', 'MS Computer Science'),
 ('CS405', 'Capstone Project', 'MS Computer Science');

-- Set Prerequisites
UPDATE course SET prerequisite_id = (SELECT id FROM course WHERE code = 'CS101') WHERE code IN ('CS102', 'CS103', 'CS104', 'CS105');
UPDATE course SET prerequisite_id = (SELECT id FROM course WHERE code = 'CS102') WHERE code IN ('CS201', 'CS202');
UPDATE course SET prerequisite_id = (SELECT id FROM course WHERE code = 'CS103') WHERE code = 'CS203';
UPDATE course SET prerequisite_id = (SELECT id FROM course WHERE code = 'CS104') WHERE code = 'CS204';
UPDATE course SET prerequisite_id = (SELECT id FROM course WHERE code = 'CS105') WHERE code = 'CS205';
UPDATE course SET prerequisite_id = (SELECT id FROM course WHERE code = 'CS201') WHERE code IN ('CS301', 'CS302');
UPDATE course SET prerequisite_id = (SELECT id FROM course WHERE code = 'CS203') WHERE code = 'CS304';
UPDATE course SET prerequisite_id = (SELECT id FROM course WHERE code = 'CS204') WHERE code = 'CS305';
UPDATE course SET prerequisite_id = (SELECT id FROM course WHERE code = 'CS301') WHERE code IN ('CS401', 'CS402');
UPDATE course SET prerequisite_id = (SELECT id FROM course WHERE code = 'CS305') WHERE code IN ('CS403', 'CS404');
UPDATE course SET prerequisite_id = (SELECT id FROM course WHERE code = 'CS402') WHERE code = 'CS405';


INSERT OR IGNORE INTO student (email, password, name, degree, year, gpa)
VALUES ('antara@example.com', 'mypassword', 'Antara Bhavsar', 'MS Computer Science', 1, 3.9);

INSERT OR IGNORE INTO student_course (student_id, course_id) VALUES
(1, 1),
(1, 2),
(1, 3);
