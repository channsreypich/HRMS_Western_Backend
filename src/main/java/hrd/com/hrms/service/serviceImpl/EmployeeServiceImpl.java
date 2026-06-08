package hrd.com.hrms.service.serviceImpl;

import hrd.com.hrms.dto.request.EmployeeRequest;
import hrd.com.hrms.dto.response.EmployeeResponse;
import hrd.com.hrms.dto.response.EmployeeDetailResponse; // Added for screen synchronization
import hrd.com.hrms.exception.BadRequestException;
import hrd.com.hrms.exception.ResourceNotFoundException;
import hrd.com.hrms.mapper.EmployeeMapper;
import hrd.com.hrms.model.*;
import hrd.com.hrms.repository.*;
import hrd.com.hrms.service.EmployeeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;

@Service
@Transactional
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final DepartmentRepository departmentRepository;
    private final PositionRepository positionRepository;
    private final EmployeeMapper employeeMapper;
    private final PasswordEncoder passwordEncoder;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository, UserRepository userRepository,
                               RoleRepository roleRepository, DepartmentRepository departmentRepository,
                               PositionRepository positionRepository, EmployeeMapper employeeMapper,
                               PasswordEncoder passwordEncoder) {
        this.employeeRepository = employeeRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.departmentRepository = departmentRepository;
        this.positionRepository = positionRepository;
        this.employeeMapper = employeeMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public EmployeeResponse createEmployee(EmployeeRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email is already taken!");
        }
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new BadRequestException("Username is already taken!");
        }
        if (request.getPassword() == null || request.getPassword().isBlank()) {
            throw new BadRequestException("Password is required when creating an employee account.");
        }

        Role role = roleRepository.findById(request.getRoleId())
                .orElseThrow(() -> new ResourceNotFoundException("Role configuration not found"));
        Department dept = departmentRepository.findById(request.getDepartmentId()).orElse(null);
        Position pos = positionRepository.findById(request.getPositionId()).orElse(null);

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(role)
                .isActive(true)
                .build();

        Employee employee = Employee.builder()
                .user(user)
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .department(dept)
                .position(pos)
                .phone(request.getPhone()) // Maps from view inputs
                .status(request.getStatus() != null ? request.getStatus().toLowerCase() : "active")
                .hireDate(request.getHireDate())
                .baseSalary(request.getBaseSalary())
                .build();

        return employeeMapper.toResponse(employeeRepository.save(employee));
    }

    @Override
    public EmployeeResponse updateEmployee(UUID id, EmployeeRequest request) {
        Employee emp = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));

        Department dept = departmentRepository.findById(request.getDepartmentId()).orElse(null);
        Position pos = positionRepository.findById(request.getPositionId()).orElse(null);

        emp.setFirstName(request.getFirstName());
        emp.setLastName(request.getLastName());
        emp.setDepartment(dept);
        emp.setPosition(pos);
        emp.setPhone(request.getPhone());
        if (request.getStatus() != null) {
            emp.setStatus(request.getStatus().toLowerCase());
        }
        emp.setHireDate(request.getHireDate());
        emp.setBaseSalary(request.getBaseSalary());

        User user = emp.getUser();
        user.setEmail(request.getEmail());
        user.setUsername(request.getUsername());

        if (request.getRoleId() != null) {
            Role role = roleRepository.findById(request.getRoleId())
                    .orElseThrow(() -> new ResourceNotFoundException("Role configuration not found"));
            user.setRole(role);
        }

        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        return employeeMapper.toResponse(employeeRepository.save(emp));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EmployeeResponse> getAllEmployees(Pageable pageable) {
        return employeeRepository.findAll(pageable).map(employeeMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public EmployeeResponse getEmployeeById(UUID id) {
        return employeeRepository.findById(id)
                .map(employeeMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
    }

    // ── Added Custom Profile Breakdown for EmployeeDetail.vue Layout ──
    @Override
    @Transactional(readOnly = true)
    public EmployeeDetailResponse getEmployeeDetailsById(UUID id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee profile info not found for ID: " + id));

        return EmployeeDetailResponse.builder()
                .id(employee.getId())
                .firstName(employee.getFirstName())
                .lastName(employee.getLastName())
                .phone(employee.getPhone())
                .status(employee.getStatus() != null ? employee.getStatus().toLowerCase() : "inactive")
                .hireDate(employee.getHireDate())
                .baseSalary(employee.getBaseSalary())
                // Safe checks to avoid lazy loading/NPE crashes
                .email(employee.getUser() != null ? employee.getUser().getEmail() : "N/A")
                .departmentName(employee.getDepartment() != null ? employee.getDepartment().getName() : null)
                .positionTitle(employee.getPosition() != null ? employee.getPosition().getName() : null)
                .build();
    }

    @Override
    public void deleteEmployee(UUID id) {
        Employee emp = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
        employeeRepository.delete(emp);
    }
}