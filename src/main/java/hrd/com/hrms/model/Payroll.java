package hrd.com.hrms.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "payroll")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payroll {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @Column(name = "pay_period_start")
    private LocalDate payPeriodStart;

    @Column(name = "pay_period_end")
    private LocalDate payPeriodEnd;

    @Column(name = "payment_date")
    private LocalDate paymentDate;

    @Column(name = "basic_salary", nullable = false,precision = 19, scale = 2)
    private BigDecimal basicSalary;

    @Column(nullable = false,precision = 19, scale = 2)
    private BigDecimal allowances;

    @Column(nullable = false,precision = 19, scale = 2)
    private BigDecimal deductions;

    @Column(precision = 19, scale = 2)
    private BigDecimal netPay;

    // Payment lifecycle state: "draft" (generated, not yet paid) or "paid".
    @Column(nullable = false)
    @Builder.Default
    private String status = "draft";
}
