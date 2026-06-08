package hrd.com.hrms.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
public class PositionResponse {
    private UUID id;
    private String title;
    private BigDecimal baseSalary;
    private UUID departmentId;
    private String departmentName;
}