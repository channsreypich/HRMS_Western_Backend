package hrd.com.hrms.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserResponse(
        UUID id,
        String first_name,
        String last_name,
        String email,
        String role,
        String status,
        LocalDateTime created_at
) {}