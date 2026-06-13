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
                .username(employee.getUser() != null ? employee.getUser().getUsername() : null)
                .email(employee.getUser() != null ? employee.getUser().getEmail() : null)
                .employeeCode(employee.getEmployeeCode())
                .firstName(employee.getFirstName())
                .lastName(employee.getLastName())
                .phone(employee.getPhone())
                .roleName(employee.getUser() != null && employee.getUser().getRole() != null
                        ? employee.getUser().getRole().getName().name() : null)
                .departmentId(employee.getDepartment() != null ? employee.getDepartment().getId() : null)
                .departmentName(employee.getDepartment() != null ? employee.getDepartment().getName() : "N/A")
                .positionId(employee.getPosition() != null ? employee.getPosition().getId() : null)
                .positionTitle(employee.getPosition() != null ? employee.getPosition().getTitle() : "N/A")
                .isActive(employee.getUser() != null && employee.getUser().isActive())
                .status(employee.getStatus())
                .hireDate(employee.getHireDate())
                .baseSalary(employee.getBaseSalary())
                .faceEnrolled(employee.getFaceDescriptor() != null && !employee.getFaceDescriptor().isBlank())
                .documents(employee.getDocuments())
                .build();
    }
}