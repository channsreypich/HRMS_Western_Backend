-- =========================================================================
-- 1. SEED ROLES (3 Records)
-- =========================================================================
INSERT INTO roles (name) VALUES
                             ('ROLE_ADMIN'),
                             ('ROLE_HR'),
                             ('ROLE_EMPLOYEE');

-- =========================================================================
-- 2. SEED DEPARTMENTS (4 Records)
-- =========================================================================
INSERT INTO departments (code, name) VALUES
                                         ('DEPT-IT', 'Information Technology'),
                                         ('DEPT-HR', 'Human Resources'),
                                         ('DEPT-FIN', 'Finance & Payroll'),
                                         ('DEPT-OPS', 'Operations Management');

-- =========================================================================
-- 3. SEED POSITIONS (10 Records)
-- =========================================================================
INSERT INTO positions (title, department_id) VALUES
                                                 ('CTO', (SELECT id FROM departments WHERE code = 'DEPT-IT')),
                                                 ('Lead Full-Stack Developer', (SELECT id FROM departments WHERE code = 'DEPT-IT')),
                                                 ('Senior Backend Engineer', (SELECT id FROM departments WHERE code = 'DEPT-IT')),
                                                 ('UI/UX Designer', (SELECT id FROM departments WHERE code = 'DEPT-IT')),
                                                 ('HR Director', (SELECT id FROM departments WHERE code = 'DEPT-HR')),
                                                 ('Talent Acquisition Specialist', (SELECT id FROM departments WHERE code = 'DEPT-HR')),
                                                 ('Financial Controller', (SELECT id FROM departments WHERE code = 'DEPT-FIN')),
                                                 ('Payroll Accountant', (SELECT id FROM departments WHERE code = 'DEPT-FIN')),
                                                 ('Operations Manager', (SELECT id FROM departments WHERE code = 'DEPT-OPS')),
                                                 ('Support Specialist', (SELECT id FROM departments WHERE code = 'DEPT-OPS'));

-- =========================================================================
-- 4. SEED USERS (10 Records - passwords are pre-hashed hashes for 'password')
-- =========================================================================
INSERT INTO users (username, email, password, role_id) VALUES
                                                           ('admin.pich', 'admin@hrms.com', '$2a$10$X5p8uUqgMkbK0fWc3H9XmO/L1w7tA8F9dK1L.b3g1M2c3d4e5f6g7', (SELECT id FROM roles WHERE name = 'ROLE_ADMIN')),
                                                           ('hr.sokha', 'sokha.hr@hrms.com', '$2a$10$X5p8uUqgMkbK0fWc3H9XmO/L1w7tA8F9dK1L.b3g1M2c3d4e5f6g7', (SELECT id FROM roles WHERE name = 'ROLE_HR')),
                                                           ('dev.rath', 'rath.dev@hrms.com', '$2a$10$X5p8uUqgMkbK0fWc3H9XmO/L1w7tA8F9dK1L.b3g1M2c3d4e5f6g7', (SELECT id FROM roles WHERE name = 'ROLE_EMPLOYEE')),
                                                           ('designer.lina', 'lina.design@hrms.com', '$2a$10$X5p8uUqgMkbK0fWc3H9XmO/L1w7tA8F9dK1L.b3g1M2c3d4e5f6g7', (SELECT id FROM roles WHERE name = 'ROLE_EMPLOYEE')),
                                                           ('fin.borin', 'borin.fin@hrms.com', '$2a$10$X5p8uUqgMkbK0fWc3H9XmO/L1w7tA8F9dK1L.b3g1M2c3d4e5f6g7', (SELECT id FROM roles WHERE name = 'ROLE_EMPLOYEE')),
                                                           ('ops.vanna', 'vanna.ops@hrms.com', '$2a$10$X5p8uUqgMkbK0fWc3H9XmO/L1w7tA8F9dK1L.b3g1M2c3d4e5f6g7', (SELECT id FROM roles WHERE name = 'ROLE_EMPLOYEE')),
                                                           ('dev.chitra', 'chitra.dev@hrms.com', '$2a$10$X5p8uUqgMkbK0fWc3H9XmO/L1w7tA8F9dK1L.b3g1M2c3d4e5f6g7', (SELECT id FROM roles WHERE name = 'ROLE_EMPLOYEE')),
                                                           ('payroll.chavy', 'chavy.pay@hrms.com', '$2a$10$X5p8uUqgMkbK0fWc3H9XmO/L1w7tA8F9dK1L.b3g1M2c3d4e5f6g7', (SELECT id FROM roles WHERE name = 'ROLE_HR')),
                                                           ('ops.dara', 'dara.ops@hrms.com', '$2a$10$X5p8uUqgMkbK0fWc3H9XmO/L1w7tA8F9dK1L.b3g1M2c3d4e5f6g7', (SELECT id FROM roles WHERE name = 'ROLE_EMPLOYEE')),
                                                           ('support.nita', 'nita.sup@hrms.com', '$2a$10$X5p8uUqgMkbK0fWc3H9XmO/L1w7tA8F9dK1L.b3g1M2c3d4e5f6g7', (SELECT id FROM roles WHERE name = 'ROLE_EMPLOYEE'));

-- =========================================================================
-- 5. SEED EMPLOYEES (10 Records mapped exactly to user structures)
-- =========================================================================
INSERT INTO employees (user_id, first_name, last_name, gender, date_of_birth, phone_number, hire_date, position_id, salary) VALUES
                                                                                                                                ((SELECT id FROM users WHERE username = 'admin.pich'), 'Chansreypich', 'Chhun', 'FEMALE', '2002-05-12', '+85512345678', '2025-01-10', (SELECT id FROM positions WHERE title = 'CTO'), 4500.00),
                                                                                                                                ((SELECT id FROM users WHERE username = 'hr.sokha'), 'Sokha', 'Meas', 'MALE', '1995-08-20', '+85512888999', '2025-02-15', (SELECT id FROM positions WHERE title = 'HR Director'), 1800.00),
                                                                                                                                ((SELECT id FROM users WHERE username = 'dev.rath'), 'Rath', 'Chan', 'MALE', '1998-11-02', '+85599111222', '2025-03-01', (SELECT id FROM positions WHERE title = 'Lead Full-Stack Developer'), 2800.00),
                                                                                                                                ((SELECT id FROM users WHERE username = 'designer.lina'), 'Lina', 'Sok', 'FEMALE', '2000-01-25', '+85577333444', '2025-03-15', (SELECT id FROM positions WHERE title = 'UI/UX Designer'), 1400.00),
                                                                                                                                ((SELECT id FROM users WHERE username = 'fin.borin'), 'Borin', 'Khem', 'MALE', '1993-04-14', '+85510555666', '2025-01-20', (SELECT id FROM positions WHERE title = 'Financial Controller'), 2200.00),
                                                                                                                                ((SELECT id FROM users WHERE username = 'ops.vanna'), 'Vanna', 'Keo', 'MALE', '1996-07-30', '+85588777888', '2025-04-01', (SELECT id FROM positions WHERE title = 'Operations Manager'), 1900.00),
                                                                                                                                ((SELECT id FROM users WHERE username = 'dev.chitra'), 'Chitra', 'Ouk', 'FEMALE', '2001-09-09', '+85515999000', '2025-05-01', (SELECT id FROM positions WHERE title = 'Senior Backend Engineer'), 2000.00),
                                                                                                                                ((SELECT id FROM users WHERE username = 'payroll.chavy'), 'Chavy', 'Heng', 'FEMALE', '1997-12-12', '+85511222333', '2025-02-20', (SELECT id FROM positions WHERE title = 'Payroll Accountant'), 1200.00),
                                                                                                                                ((SELECT id FROM users WHERE username = 'ops.dara'), 'Dara', 'Prom', 'MALE', '1999-03-03', '+85516444555', '2025-05-10', (SELECT id FROM positions WHERE title = 'Support Specialist'), 850.00),
                                                                                                                                ((SELECT id FROM users WHERE username = 'support.nita'), 'Nita', 'Chea', 'FEMALE', '2002-06-18', '+85592666777', '2025-05-15', (SELECT id FROM positions WHERE title = 'Support Specialist'), 850.00);

-- =========================================================================
-- 6. SEED ATTENDANCE (10 Records - Sample data for June 1st, 2026)
-- =========================================================================
INSERT INTO attendance (employee_id, date, clock_in, clock_out, status) VALUES
                                                                            ((SELECT id FROM employees WHERE first_name = 'Chansreypich'), '2026-06-01', '2026-06-01 07:55:00', '2026-06-01 17:05:00', 'PRESENT'),
                                                                            ((SELECT id FROM employees WHERE first_name = 'Sokha'), '2026-06-01', '2026-06-01 08:02:00', '2026-06-01 17:01:00', 'PRESENT'),
                                                                            ((SELECT id FROM employees WHERE first_name = 'Rath'), '2026-06-01', '2026-06-01 07:45:00', '2026-06-01 17:30:00', 'PRESENT'),
                                                                            ((SELECT id FROM employees WHERE first_name = 'Lina'), '2026-06-01', '2026-06-01 08:15:00', '2026-06-01 17:00:00', 'LATE'),
                                                                            ((SELECT id FROM employees WHERE first_name = 'Borin'), '2026-06-01', '2026-06-01 07:50:00', '2026-06-01 17:00:00', 'PRESENT'),
                                                                            ((SELECT id FROM employees WHERE first_name = 'Vanna'), '2026-06-01', NULL, NULL, 'ABSENT'),
                                                                            ((SELECT id FROM employees WHERE first_name = 'Chitra'), '2026-06-01', '2026-06-01 07:58:00', '2026-06-01 17:00:00', 'PRESENT'),
                                                                            ((SELECT id FROM employees WHERE first_name = 'Chavy'), '2026-06-01', '2026-06-01 08:00:00', '2026-06-01 17:00:00', 'PRESENT'),
                                                                            ((SELECT id FROM employees WHERE first_name = 'Dara'), '2026-06-01', '2026-06-01 07:40:00', '2026-06-01 17:00:00', 'PRESENT'),
                                                                            ((SELECT id FROM employees WHERE first_name = 'Nita'), '2026-06-01', '2026-06-01 08:05:00', '2026-06-01 17:02:00', 'PRESENT');

-- =========================================================================
-- 7. SEED LEAVE REQUESTS (10 Records)
-- =========================================================================
INSERT INTO leave_requests (employee_id, leave_type, start_date, end_date, reason, status) VALUES
                                                                                               ((SELECT id FROM employees WHERE first_name = 'Vanna'), 'SICK_LEAVE', '2026-06-01', '2026-06-02', 'High fever and flu symptoms', 'APPROVED'),
                                                                                               ((SELECT id FROM employees WHERE first_name = 'Lina'), 'ANNUAL_LEAVE', '2026-06-10', '2026-06-12', 'Family reunion trip', 'PENDING'),
                                                                                               ((SELECT id FROM employees WHERE first_name = 'Rath'), 'CASUAL_LEAVE', '2026-05-02', '2026-05-02', 'Urgent personal paperwork business', 'APPROVED'),
                                                                                               ((SELECT id FROM employees WHERE first_name = 'Chitra'), 'ANNUAL_LEAVE', '2026-07-01', '2026-07-05', 'Summer vacation rest', 'PENDING'),
                                                                                               ((SELECT id FROM employees WHERE first_name = 'Borin'), 'SICK_LEAVE', '2026-02-10', '2026-02-11', 'Dental extraction surgery recovery', 'APPROVED'),
                                                                                               ((SELECT id FROM employees WHERE first_name = 'Dara'), 'CASUAL_LEAVE', '2026-06-15', '2026-06-15', 'Moving to a new apartment', 'PENDING'),
                                                                                               ((SELECT id FROM employees WHERE first_name = 'Chavy'), 'ANNUAL_LEAVE', '2026-04-12', '2026-04-15', 'Traditional holiday celebration extra days', 'APPROVED'),
                                                                                               ((SELECT id FROM employees WHERE first_name = 'Nita'), 'SICK_LEAVE', '2026-03-05', '2026-03-05', 'Medical checkup appointment', 'APPROVED'),
                                                                                               ((SELECT id FROM employees WHERE first_name = 'Sokha'), 'CASUAL_LEAVE', '2026-06-20', '2026-06-21', 'Attending a professional seminar outside', 'PENDING'),
                                                                                               ((SELECT id FROM employees WHERE first_name = 'Chansreypich'), 'ANNUAL_LEAVE', '2026-08-20', '2026-08-25', 'Personal time off research travel', 'PENDING');

-- =========================================================================
-- 8. SEED PAYROLL (10 Records - May 2026 Cycle Processing calculation metrics)
-- =========================================================================
INSERT INTO payroll (employee_id, pay_period_start, pay_period_end, basic_salary, allowances, deductions, net_pay, payment_date) VALUES
                                                                                                                                     ((SELECT id FROM employees WHERE first_name = 'Chansreypich'), '2026-05-01', '2026-05-31', 4500.00, 200.00, 0.00, 4700.00, '2026-05-31'),
                                                                                                                                     ((SELECT id FROM employees WHERE first_name = 'Sokha'), '2026-05-01', '2026-05-31', 1800.00, 100.00, 50.00, 1850.00, '2026-05-31'),
                                                                                                                                     ((SELECT id FROM employees WHERE first_name = 'Rath'), '2026-05-01', '2026-05-31', 2800.00, 150.00, 0.00, 2950.00, '2026-05-31'),
                                                                                                                                     ((SELECT id FROM employees WHERE first_name = 'Lina'), '2026-05-01', '2026-05-31', 1400.00, 50.00, 20.00, 1430.00, '2026-05-31'),
                                                                                                                                     ((SELECT id FROM employees WHERE first_name = 'Borin'), '2026-05-01', '2026-05-31', 2200.00, 120.00, 0.00, 2320.00, '2026-05-31'),
                                                                                                                                     ((SELECT id FROM employees WHERE first_name = 'Vanna'), '2026-05-01', '2026-05-31', 1900.00, 100.00, 80.00, 1920.00, '2026-05-31'),
                                                                                                                                     ((SELECT id FROM employees WHERE first_name = 'Chitra'), '2026-05-01', '2026-05-31', 2000.00, 100.00, 0.00, 2100.00, '2026-05-31'),
                                                                                                                                     ((SELECT id FROM employees WHERE first_name = 'Chavy'), '2026-05-01', '2026-05-31', 1200.00, 50.00, 0.00, 1250.00, '2026-05-31'),
                                                                                                                                     ((SELECT id FROM employees WHERE first_name = 'Dara'), '2026-05-01', '2026-05-31', 850.00, 30.00, 10.00, 870.00, '2026-05-31'),
                                                                                                                                     ((SELECT id FROM employees WHERE first_name = 'Nita'), '2026-05-01', '2026-05-31', 850.00, 30.00, 0.00, 880.00, '2026-05-31');