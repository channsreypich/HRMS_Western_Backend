package hrd.com.hrms.dto.request;

import jakarta.validation.constraints.NotBlank;

// Triggers bulk payroll generation for a calendar month. With snake_case JSON this maps to { "payment_month": "2026-06" }.
public record MonthlyPayrollRequest(
        @NotBlank(message = "payment_month is required (format YYYY-MM)")
        String paymentMonth
) {}
