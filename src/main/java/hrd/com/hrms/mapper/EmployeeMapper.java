package hrd.com.hrms.mapper;

import hrd.com.hrms.dto.request.EmployeeRequest;
import hrd.com.hrms.dto.response.EmployeeResponse;
import hrd.com.hrms.model.Employee;
import org.springframework.stereotype.Component;

@Component
public class EmployeeMapper { // Removed the generic <EmployeeResponse>

    // Removed the generic <EmployeeRequest> from the method signature
    public Employee toEntity(EmployeeRequest request) {
        if (request == null) return null;

        Employee employee = new Employee();
        employee.setFirstName(request.getFirstName());
        employee.setLastName(request.getLastName());
        employee.setEmail(request.getEmail());
        employee.setHireDate(request.getHireDate());
        return employee;
    }

    public EmployeeResponse toResponse(Employee employee) {
        if (employee == null) return null;

        String deptName = (employee.getDepartment() != null) ? employee.getDepartment().getName() : "Unassigned";

        return new EmployeeResponse(
                employee.getId(),
                employee.getFirstName(),
                employee.getLastName(),
                employee.getEmail(),
                employee.getHireDate(),
                deptName
        );
    }
}