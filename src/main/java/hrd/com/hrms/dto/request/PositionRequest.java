package hrd.com.hrms.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class PositionRequest {
    @NotBlank(message = "Position title is required")
    private String title;

    @NotNull(message = "Base salary is required")
    @PositiveOrZero(message = "Base salary must be zero or greater")
    private BigDecimal baseSalary;

    private UUID departmentId;
}