CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- 1. Roles
CREATE TABLE roles (
                       id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                       name VARCHAR(50) UNIQUE NOT NULL
);

-- 2. Departments
CREATE TABLE departments (
                             id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                             code VARCHAR(20) UNIQUE NOT NULL,
                             name VARCHAR(100) NOT NULL
);

-- 3. Positions
CREATE TABLE positions (
                           id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                           title VARCHAR(100) NOT NULL,
                           department_id UUID REFERENCES departments(id) ON DELETE SET NULL
);

-- 4. Users
CREATE TABLE users (
                       id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                       username VARCHAR(50) UNIQUE NOT NULL,
                       email VARCHAR(100) UNIQUE NOT NULL,
                       password VARCHAR(255) NOT NULL,
                       is_active BOOLEAN DEFAULT TRUE,
                       role_id UUID REFERENCES roles(id) ON DELETE RESTRICT
);

-- 5. Employees
CREATE TABLE employees (
                           id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                           user_id UUID UNIQUE REFERENCES users(id) ON DELETE CASCADE,
                           first_name VARCHAR(50) NOT NULL,
                           last_name VARCHAR(50) NOT NULL,
                           gender VARCHAR(10) NOT NULL,
                           date_of_birth DATE NOT NULL,
                           phone_number VARCHAR(20),
                           hire_date DATE NOT NULL,
                           position_id UUID REFERENCES positions(id) ON DELETE RESTRICT,
                           salary NUMERIC(12, 2) NOT NULL DEFAULT 0.00
);

-- 6. Attendance
CREATE TABLE attendance (
                            id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                            employee_id UUID NOT NULL REFERENCES employees(id) ON DELETE CASCADE,
                            date DATE NOT NULL,
                            clock_in TIMESTAMP WITHOUT TIME ZONE,
                            clock_out TIMESTAMP WITHOUT TIME ZONE,
                            status VARCHAR(20) NOT NULL DEFAULT 'PRESENT'
);

-- 7. Leave Requests
CREATE TABLE leave_requests (
                                id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                                employee_id UUID NOT NULL REFERENCES employees(id) ON DELETE CASCADE,
                                leave_type VARCHAR(30) NOT NULL,
                                start_date DATE NOT NULL,
                                end_date DATE NOT NULL,
                                reason TEXT,
                                status VARCHAR(20) NOT NULL DEFAULT 'PENDING'
);

-- 8. Payroll
CREATE TABLE payroll (
                         id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                         employee_id UUID NOT NULL REFERENCES employees(id) ON DELETE CASCADE,
                         pay_period_start DATE NOT NULL,
                         pay_period_end DATE NOT NULL,
                         basic_salary NUMERIC(12, 2) NOT NULL,
                         allowances NUMERIC(12, 2) DEFAULT 0.00,
                         deductions NUMERIC(12, 2) DEFAULT 0.00,
                         net_pay NUMERIC(12, 2) NOT NULL,
                         payment_date DATE
);