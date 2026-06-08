package hrd.com.hrms.service;

import hrd.com.hrms.dto.request.EmployeeRequest;
import hrd.com.hrms.dto.response.EmployeeDetailResponse;
import hrd.com.hrms.dto.response.EmployeeResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.UUID;

public interface EmployeeService {
    EmployeeResponse createEmployee(EmployeeRequest request);
    EmployeeResponse updateEmployee(UUID id, EmployeeRequest request);
    Page<EmployeeResponse> getAllEmployees(Pageable pageable);
    EmployeeResponse getEmployeeById(UUID id);
    EmployeeDetailResponse getEmployeeDetailsById(UUID id);
    void deleteEmployee(UUID id);
}