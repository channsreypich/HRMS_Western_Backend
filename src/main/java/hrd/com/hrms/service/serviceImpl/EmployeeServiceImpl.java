package hrd.com.hrms.service.serviceImpl;

import hrd.com.hrms.dto.request.EmployeeRequest;
import hrd.com.hrms.dto.response.EmployeeResponse;
import hrd.com.hrms.model.Employee;
import hrd.com.hrms.repository.EmployeeRepository;
import hrd.com.hrms.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;

    @Override
    public EmployeeResponse createEmployee(EmployeeRequest request) {
        Employee emp = new Employee();
        emp.setId(UUID.randomUUID());
        mapToEntity(request, emp);
        return mapToResponse(employeeRepository.save(emp));
    }

    @Override
    public EmployeeResponse updateEmployee(UUID id, EmployeeRequest request) {
        Employee emp = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        mapToEntity(request, emp);
        return mapToResponse(employeeRepository.save(emp));
    }

    @Override
    public EmployeeResponse getEmployeeById(UUID id) {
        return employeeRepository.findById(id).map(this::mapToResponse)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
    }

    @Override
    public List<EmployeeResponse> getAllEmployees() {
        return employeeRepository.findAll().stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    @Override
    public void deleteEmployee(UUID id) {
        employeeRepository.deleteById(id);
    }

    private void mapToEntity(EmployeeRequest request, Employee emp) {
        emp.setFirstName(request.getFirstName());
        emp.setLastName(request.getLastName());
        emp.setEmail(request.getEmail());
        emp.setPhoneNumber(request.getPhoneNumber());
        emp.setHireDate(request.getHireDate());
        emp.setDepartmentId(request.getDepartmentId());
        emp.setPositionId(request.getPositionId());
    }

    private EmployeeResponse mapToResponse(Employee emp) {
        EmployeeResponse resp = new EmployeeResponse();
        resp.setId(emp.getId());
        resp.setFirstName(emp.getFirstName());
        resp.setLastName(emp.getLastName());
        resp.setFullName(emp.getFirstName() + " " + emp.getLastName());
        resp.setEmail(emp.getEmail());
        resp.setPhoneNumber(emp.getPhoneNumber());
        resp.setHireDate(emp.getHireDate());
        resp.setDepartmentId(emp.getDepartmentId());
        resp.setPositionId(emp.getPositionId());
        return resp;
    }
}
