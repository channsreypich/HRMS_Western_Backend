package hrd.com.hrms.dto.response;

import lombok.Data;
import java.util.UUID;

@Data
public class LoginResponse {
    private String token;
    private String tokenType = "Bearer";
    private UUID userId;
    private String username;
    private String roleName;
}