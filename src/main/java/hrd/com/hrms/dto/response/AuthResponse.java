package hrd.com.hrms.dto.response;

import java.util.List;

public record AuthResponse(
        String accessToken,
        String tokenType,
        String email,
        List<String> roles
) {
    public AuthResponse(String accessToken, String email, List<String> roles) {
        this(accessToken, "Bearer", email, roles);
    }
}