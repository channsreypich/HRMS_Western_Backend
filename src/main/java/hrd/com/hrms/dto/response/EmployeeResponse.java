package hrd.com.hrms.dto.response;

import lombok.Data;
import java.time.LocalDate;
import java.util.UUID;
@Data
public class EmployeeResponse {
    private UUID id;
    private String firstName;
    private String lastName;
    private String fullName;
    private String email;
    private String phoneNumber;
    private LocalDate hireDate;
    private UUID departmentId;
    private UUID positionId;

    public EmployeeResponse(UUID id, String firstName, String lastName, String email, LocalDate hireDate, String deptName) {
    }
}
