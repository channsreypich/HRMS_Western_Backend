package hrd.com.hrms.dto.response;

import java.util.UUID;

public record UserResponse(
        UUID id,         // Changed from Long to UUID
        String name,
        String email,
        String role,
        boolean active
) {}