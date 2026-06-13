package hrd.com.hrms.service.serviceImpl;

import hrd.com.hrms.dto.request.EmployeeRequest;
import hrd.com.hrms.dto.response.EmployeeResponse;
import hrd.com.hrms.dto.response.EmployeeDetailResponse;
import hrd.com.hrms.exception.BadRequestException;
import hrd.com.hrms.exception.ResourceNotFoundException;
import hrd.com.hrms.mapper.EmployeeMapper;
import hrd.com.hrms.model.*;
import hrd.com.hrms.repository.*;
import hrd.com.hrms.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final DepartmentRepository departmentRepository;
    private final PositionRepository positionRepository;
    private final EmployeeMapper employeeMapper;
    private final PasswordEncoder passwordEncoder;
    private final EmployeeDocumentRepository documentRepository;

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

        // Role is optional from the UI; default new employees to ROLE_EMPLOYEE
        Role role = (request.getRoleId() != null)
                ? roleRepository.findById(request.getRoleId())
                    .orElseThrow(() -> new ResourceNotFoundException("Role not found"))
                : roleRepository.findByName(hrd.com.hrms.enums.RoleName.ROLE_EMPLOYEE)
                    .orElseThrow(() -> new ResourceNotFoundException("Default ROLE_EMPLOYEE not found"));
        Department dept = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Department not found"));
        Position pos = positionRepository.findById(request.getPositionId())
                .orElseThrow(() -> new ResourceNotFoundException("Position not found"));

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(role)
                .isActive(true)
                .build();
        String generatedCode = generateEmployeeCode();

        Employee employee = Employee.builder()
                .user(user)
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .department(dept)
                .position(pos)
                .employeeCode(generatedCode)
                .phone(request.getPhone())
                .status(request.getStatus() != null ? request.getStatus().toLowerCase() : "active")
                .hireDate(request.getHireDate())
                .baseSalary(request.getBaseSalary())
                .build();

        return employeeMapper.toResponse(employeeRepository.save(employee));
    }

    // Produces the next clean sequential code (EMP-001, EMP-002, ...).
    // Only well-formed short codes feed the counter, so legacy timestamp codes
    // are ignored and the new code never collides with the seeded EMP-00x set.
    private String generateEmployeeCode() {
        int max = employeeRepository.findAll().stream()
                .map(Employee::getEmployeeCode)
                .filter(c -> c != null && c.matches("(?i)EMP-?\\d{1,5}"))
                .map(c -> c.replaceAll("\\D", ""))
                .filter(s -> !s.isEmpty())
                .mapToInt(Integer::parseInt)
                .max()
                .orElse(0);
        return String.format("EMP-%03d", max + 1);
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
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());

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
                .email(employee.getUser() != null ? employee.getUser().getEmail() : "N/A")
                .departmentName(employee.getDepartment() != null ? employee.getDepartment().getName() : null)
                // Use .getTitle() here if your Position entity uses 'title'
                .positionTitle(employee.getPosition() != null ? employee.getPosition().getTitle() : null)
                .build();
    }

    @Override
    public void deleteEmployee(UUID id) {
        Employee emp = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
        employeeRepository.delete(emp);
    }

    @Override
    public void uploadDocument(UUID employeeId, EmployeeDocument document) {
        Employee emp = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
        document.setEmployee(emp);
        documentRepository.save(document);
    }

    @Override
    public void deleteDocument(UUID documentId) {
        if (!documentRepository.existsById(documentId)) {
            throw new ResourceNotFoundException("Document not found");
        }
        documentRepository.deleteById(documentId);
    }
}