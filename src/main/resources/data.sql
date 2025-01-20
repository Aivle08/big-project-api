-- Company 테이블 데이터 삽입
INSERT INTO company (name, address)
VALUES
    ('Tech Solutions', '5678 Tech Avenue'),
    ('Innovative Corp', '9101 Innovation Street'),
    ('Global Enterprises', '2345 Global Road');

-- Department 테이블 데이터 삽입
INSERT INTO department (name, company_id)
VALUES
    ('Software Development', 1),
    ('Human Resources', 1),
    ('Research & Development', 2),
    ('Marketing', 3),
    ('Finance', 3);

-- Users 테이블 데이터 삽입
INSERT INTO users (username, password, name, email, position, company_id)
VALUES
    ('johndoe', 'securepassword', 'John Doe', 'johndoe@example.com', 'Manager', 1),
    ('janedoe', 'password123', 'Jane Doe', 'janedoe@example.com', 'HR Specialist', 2),
    ('samsmith', 'password456', 'Sam Smith', 'samsmith@example.com', 'Developer', 1),
    ('mikejohnson', 'mypassword', 'Mike Johnson', 'mikejohnson@example.com', 'Marketing Lead', 1),
    ('emilydavids', 'admin123', 'Emily Davids', 'emilydavids@example.com', 'Finance Officer', 1);

-- Recruitment 테이블 데이터 삽입
INSERT INTO recruitment (created_date, updated_date, title, job)
VALUES
    (TIMESTAMP '2025-01-09 10:30:00', TIMESTAMP '2025-01-09 10:30:00', 'Software Engineer', 'Develop and maintain software applications'),
    (TIMESTAMP '2025-01-10 11:00:00', TIMESTAMP '2025-01-10 11:00:00', 'HR Manager', 'Manage recruitment and employee relations'),
    (TIMESTAMP '2025-01-11 12:00:00', TIMESTAMP '2025-01-11 12:00:00', 'Data Scientist', 'Analyze and interpret complex data'),
    (TIMESTAMP '2025-01-12 13:30:00', TIMESTAMP '2025-01-12 13:30:00', 'Marketing Strategist', 'Create and execute marketing plans');

-- Evaluation 테이블 데이터 삽입
INSERT INTO evaluation (item, detail, department_id, recruitment_id)
VALUES
    ('Code Quality', 'Evaluate the quality of the code written by the candidate', 1, 1),
    ('Communication Skills', 'Evaluate the ability to communicate effectively with team members', 1, 1),
    ('Problem-Solving', 'Assess the candidate''s ability to solve complex problems', 1, 1),
    ('Cultural Fit', 'Evaluate how well the candidate fits into the company culture', 1, 1);

-- Applicant 테이블 데이터 삽입
INSERT INTO applicant (name, email, contact, file_name, resume_result, resume_summary, department_id)
VALUES
    ('John Doe', 'john.doe@example.com', '123-456-7890', 'resume_john_doe.pdf', true, 'Experienced software developer', 1),
    ('Jane Smith', 'jane.smith@example.com', '234-567-8901', 'resume_jane_smith.pdf', false, 'Junior developer seeking new opportunities', 1),
    ('Alice Brown', 'alice.brown@example.com', '345-678-9012', 'resume_alice_brown.pdf', true, 'Experienced marketer with strong strategic skills', 1),
    ('Bob White', 'bob.white@example.com', '456-789-0123', 'resume_bob_white.pdf', true, 'Passionate about data science and machine learning', 1);

-- EvaluationDetail 테이블 데이터 삽입
INSERT INTO evaluation_detail (summary)
VALUES
    ('This is a detailed summary for John Doe''s evaluation'),
    ('This is a detailed summary for Jane Smith''s evaluation'),
    ('This is a detailed summary for Alice Brown''s evaluation'),
    ('This is a detailed summary for Bob White''s evaluation');


-- EvaluationScore 테이블 데이터 삽입
INSERT INTO evaluation_score (applicant_id, score, evaluation_id, evaluation_detail_id)
VALUES
    (1, 85,1, 1),
    (2, 70, 1, 2),
    (3, 95, 1, 3),
    (4, 80, 1, 4);
