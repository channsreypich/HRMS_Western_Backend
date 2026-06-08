package hrd.com.hrms.mapper;

import hrd.com.hrms.dto.response.EmployeeResponse;
import hrd.com.hrms.model.Employee;
import org.springframework.stereotype.Component;

@Component
public class EmployeeMapper {

    public EmployeeResponse toResponse(Employee employee) {
        if (employee == null) return null;

        return EmployeeResponse.builder()
                .id(employee.getId())
                .username(employee.getUser().getUsername())
                .email(employee.getUser().getEmail())
                .firstName(employee.getFirstName())
                .lastName(employee.getLastName())
                .roleName(employee.getUser().getRole().getName().name())
                .departmentName(employee.getDepartment() != null ? employee.getDepartment().getName() : "N/A")
                .positionTitle(employee.getPosition() != null ? employee.getPosition().getTitle() : "N/A")
                .isActive(employee.getUser().isActive())
                .build();
    }
}