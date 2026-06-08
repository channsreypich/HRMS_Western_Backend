package hrd.com.hrms.dto.response;

import lombok.Builder;
import lombok.Data;
import java.util.UUID;

@Data
@Builder
public class EmployeeResponse {
    private UUID id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String roleName;
    private String departmentName;
    private String positionTitle;
    private boolean isActive;
}