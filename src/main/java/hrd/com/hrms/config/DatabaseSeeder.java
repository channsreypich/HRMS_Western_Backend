package hrd.com.hrms.config;

import hrd.com.hrms.enums.AttendanceStatus;
import hrd.com.hrms.enums.LeaveStatus;
import hrd.com.hrms.enums.RoleName;
import hrd.com.hrms.model.*;
import hrd.com.hrms.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DatabaseSeeder {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;
    private final PositionRepository positionRepository;
    private final EmployeeRepository employeeRepository;
    private final AttendanceRepository attendanceRepository;
    private final LeaveRepository leaveRepository;
    private final PayrollRepository payrollRepository;
    private final PasswordEncoder passwordEncoder;

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void seed() {
        seedRoles();
        seedAdminUsers();

        // Each section is seeded independently only when its table is still empty,
        // so this safely backfills test data onto an existing database.
        List<Department> departments = ensureDepartments();
        List<Position> positions = ensurePositions(departments);
        List<Employee> employees = ensureEmployees(departments, positions);
        ensureAttendance(employees);
        ensureLeaves(employees);
        ensurePayroll(employees);
    }

    private void seedRoles() {
        for (RoleName name : RoleName.values()) {
            if (roleRepository.findByName(name).isEmpty()) {
                Role role = new Role();
                role.setName(name);
                roleRepository.save(role);
            }
        }
    }

    private void seedAdminUsers() {
        if (userRepository.count() > 0) return;
        Role adminRole = roleRepository.findByName(RoleName.ROLE_ADMIN).orElseThrow();
        Role hrRole = roleRepository.findByName(RoleName.ROLE_HR).orElseThrow();
        saveUser("Chansreypich", "Chhun", "admin.pich", "admin@hrms.com", adminRole);
        saveUser("Sokha", "Meas", "hr.sokha", "sokha.hr@hrms.com", hrRole);
        System.out.println("Seeded admin/HR login accounts (admin@hrms.com / password123).");
    }

    private List<Department> ensureDepartments() {
        if (departmentRepository.count() > 0) return departmentRepository.findAll();
        List<Department> created = new ArrayList<>();
        created.add(saveDepartment("Information Technology", "IT", "#6823ff"));
        created.add(saveDepartment("Human Resources", "HR", "#13707f"));
        created.add(saveDepartment("Finance & Accounting", "FIN", "#f59e0b"));
        return created;
    }

    private List<Position> ensurePositions(List<Department> departments) {
        if (positionRepository.count() > 0) return positionRepository.findAll();
        Department a = departments.get(0);
        Department b = departments.size() > 1 ? departments.get(1) : a;
        Department c = departments.size() > 2 ? departments.get(2) : a;
        List<Position> created = new ArrayList<>();
        created.add(savePosition("Fullstack Developer", new BigDecimal("1200.00"), a));
        created.add(savePosition("Tech Lead", new BigDecimal("2000.00"), a));
        created.add(savePosition("HR Manager", new BigDecimal("1500.00"), b));
        created.add(savePosition("Accountant", new BigDecimal("1100.00"), c));
        return created;
    }

    private List<Employee> ensureEmployees(List<Department> departments, List<Position> positions) {
        if (employeeRepository.count() > 0) return employeeRepository.findAll();
        Role employeeRole = roleRepository.findByName(RoleName.ROLE_EMPLOYEE).orElseThrow();

        // Round-robin the seeded employees across whatever departments/positions exist
        record Seed(String code, String first, String last, String email, BigDecimal salary, LocalDate hire) {}
        List<Seed> seeds = List.of(
                new Seed("EMP-001", "Dara", "Sok", "dara.sok@hrms.com", new BigDecimal("1200.00"), LocalDate.of(2025, 11, 15)),
                new Seed("EMP-002", "Sophea", "Lim", "sophea.lim@hrms.com", new BigDecimal("2000.00"), LocalDate.of(2024, 3, 10)),
                new Seed("EMP-003", "Veasna", "Chan", "veasna.chan@hrms.com", new BigDecimal("1500.00"), LocalDate.of(2025, 1, 5)),
                new Seed("EMP-004", "Nita", "Pou", "nita.pou@hrms.com", new BigDecimal("1100.00"), LocalDate.of(2026, 2, 1)),
                new Seed("EMP-005", "Rithy", "Keo", "rithy.keo@hrms.com", new BigDecimal("1200.00"), LocalDate.of(2026, 4, 20))
        );

        List<Employee> created = new ArrayList<>();
        for (int i = 0; i < seeds.size(); i++) {
            Seed s = seeds.get(i);
            Department dept = departments.isEmpty() ? null : departments.get(i % departments.size());
            Position pos = positions.isEmpty() ? null : positions.get(i % positions.size());
            created.add(saveEmployee(s.code(), s.first(), s.last(), s.email().split("@")[0], s.email(),
                    dept, pos, employeeRole, s.salary(), s.hire()));
        }
        System.out.println("Seeded " + created.size() + " sample employees.");
        return created;
    }

    private void ensureAttendance(List<Employee> employees) {
        if (attendanceRepository.count() > 0 || employees.isEmpty()) return;
        LocalDate today = LocalDate.now();
        Employee e0 = employees.get(0);
        Employee e1 = employees.size() > 1 ? employees.get(1) : e0;
        Employee e2 = employees.size() > 2 ? employees.get(2) : e0;
        saveAttendance(e0, today, today.atTime(8, 45), today.atTime(17, 5), AttendanceStatus.PRESENT);
        saveAttendance(e1, today, today.atTime(9, 20), today.atTime(18, 0), AttendanceStatus.LATE);
        saveAttendance(e2, today, today.atTime(8, 30), today.atTime(17, 0), AttendanceStatus.PRESENT);
        saveAttendance(e0, today.minusDays(1), today.minusDays(1).atTime(8, 50), today.minusDays(1).atTime(17, 0), AttendanceStatus.PRESENT);
    }

    private void ensureLeaves(List<Employee> employees) {
        if (leaveRepository.count() > 0 || employees.isEmpty()) return;
        LocalDate today = LocalDate.now();
        Employee e0 = employees.get(0);
        Employee e1 = employees.size() > 1 ? employees.get(1) : e0;
        Employee e3 = employees.size() > 3 ? employees.get(3) : e0;
        saveLeave(e0, "Annual Leave", today.plusDays(5), today.plusDays(7), "Family event", LeaveStatus.PENDING);
        saveLeave(e1, "Sick Leave", today.minusDays(3), today.minusDays(2), "Flu recovery", LeaveStatus.APPROVED);
        saveLeave(e3, "Annual Leave", today.plusDays(10), today.plusDays(12), "Personal trip", LeaveStatus.REJECTED);
    }

    private void ensurePayroll(List<Employee> employees) {
        if (payrollRepository.count() > 0 || employees.isEmpty()) return;
        LocalDate payDate = LocalDate.now().withDayOfMonth(1).minusDays(1); // end of previous month
        Employee e0 = employees.get(0);
        Employee e1 = employees.size() > 1 ? employees.get(1) : e0;
        Employee e2 = employees.size() > 2 ? employees.get(2) : e0;
        savePayroll(e0, payDate, new BigDecimal("1200.00"), new BigDecimal("100.00"), new BigDecimal("50.00"), "paid");
        savePayroll(e1, payDate, new BigDecimal("2000.00"), new BigDecimal("200.00"), new BigDecimal("80.00"), "paid");
        savePayroll(e2, payDate, new BigDecimal("1500.00"), new BigDecimal("150.00"), new BigDecimal("60.00"), "draft");
    }

    private void saveUser(String first, String last, String username, String email, Role role) {
        User user = User.builder()
                .firstName(first).lastName(last).username(username).email(email)
                .password(passwordEncoder.encode("password123"))
                .role(role).isActive(true)
                .build();
        userRepository.save(user);
    }

    private Department saveDepartment(String name, String code, String color) {
        return departmentRepository.save(Department.builder().name(name).code(code).color(color).build());
    }

    private Position savePosition(String title, BigDecimal salary, Department dept) {
        return positionRepository.save(Position.builder().title(title).baseSalary(salary).department(dept).build());
    }

    private Employee saveEmployee(String code, String first, String last, String username, String email,
                                  Department dept, Position pos, Role role, BigDecimal salary, LocalDate hireDate) {
        User user = User.builder()
                .firstName(first).lastName(last).username(username).email(email)
                .password(passwordEncoder.encode("password123"))
                .role(role).isActive(true)
                .build();
        Employee employee = Employee.builder()
                .user(user)
                .firstName(first).lastName(last)
                .department(dept).position(pos)
                .employeeCode(code)
                .status("active")
                .hireDate(hireDate)
                .baseSalary(salary)
                .build();
        return employeeRepository.save(employee);
    }

    private void saveAttendance(Employee emp, LocalDate date, LocalDateTime in, LocalDateTime out, AttendanceStatus status) {
        attendanceRepository.save(Attendance.builder()
                .employee(emp).date(date).checkIn(in).checkOut(out).status(status).scanType("SEED")
                .build());
    }

    private void saveLeave(Employee emp, String type, LocalDate start, LocalDate end, String reason, LeaveStatus status) {
        leaveRepository.save(Leave.builder()
                .employee(emp).leaveType(type).startDate(start).endDate(end).reason(reason).status(status)
                .createdAt(LocalDateTime.now())
                .build());
    }

    private void savePayroll(Employee emp, LocalDate payDate, BigDecimal basic, BigDecimal allow, BigDecimal deduct, String status) {
        payrollRepository.save(Payroll.builder()
                .employee(emp)
                .payPeriodStart(payDate.withDayOfMonth(1))
                .payPeriodEnd(payDate)
                .paymentDate(payDate)
                .basicSalary(basic).allowances(allow).deductions(deduct)
                .netPay(basic.add(allow).subtract(deduct))
                .status(status)
                .build());
    }
}
