package hrd.com.hrms.dto.response;

import lombok.Data;
import java.util.UUID;

@Data
public class RegisterResponse {
    private UUID id;
    private String username;
    private String email;
    private Boolean isActive;
    private UUID roleId;
    private String acknowledgmentToken;

    public void setMessage(String userRegisteredSuccessfully) {
    }
}