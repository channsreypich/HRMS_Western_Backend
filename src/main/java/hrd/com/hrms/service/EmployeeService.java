package hrd.com.hrms.service;

import hrd.com.hrms.dto.request.EmployeeRequest;
import hrd.com.hrms.dto.response.EmployeeResponse;
import java.util.List;
import java.util.UUID;

public interface EmployeeService {
    EmployeeResponse createEmployee(EmployeeRequest request);
    EmployeeResponse getEmployeeById(UUID id);
    List<EmployeeResponse> getAllEmployees();
    EmployeeResponse updateEmployee(UUID id, EmployeeRequest request);
    void terminateEmployee(UUID id);

    void deleteEmployee(UUID id);
}