package hrd.com.hrms.dto.response;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
public class EmployeeDetailResponse {
    private UUID id;
    private String firstName;     // Maps to employee.first_name via Jackson naming strategy
    private String lastName;      // Maps to employee.last_name
    private String email;         // Maps to employee.email (from User relation)
    private String phone;         // Maps to employee.phone
    private String status;        // Maps to employee.status
    private String departmentName;// Maps to employee.department_name
    private String positionTitle; // Maps to employee.position_title
    private LocalDate hireDate;   // Maps to employee.hire_date
    private BigDecimal baseSalary;// Maps to employee.base_salary
}