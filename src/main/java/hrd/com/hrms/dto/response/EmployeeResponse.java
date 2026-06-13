package hrd.com.hrms.dto.response;

import hrd.com.hrms.model.EmployeeDocument;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class EmployeeResponse {
    private UUID id;
    private String username;
    private String email;
    private String employeeCode;
    private String firstName;
    private String lastName;
    private String phone;
    private String roleName;
    private UUID departmentId;
    private String departmentName;
    private UUID positionId;
    private String positionTitle;
    private boolean isActive;
    private String status;
    private LocalDate hireDate;
    private BigDecimal baseSalary;
    private boolean faceEnrolled;
    private List<EmployeeDocument> documents;
}