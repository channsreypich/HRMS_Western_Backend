package hrd.com.hrms.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record PayrollStatusRequest(
        @NotBlank(message = "Status is required")
        @Pattern(regexp = "(?i)draft|paid", message = "Status must be either 'draft' or 'paid'")
        String status
) {}
