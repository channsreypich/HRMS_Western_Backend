package hrd.com.hrms.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "payrolls")
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

    @Column(name = "basic_salary", nullable = false)
    private double basicSalary;

    @Column(nullable = false)
    private double allowances;

    @Column(nullable = false)
    private double deductions;

    @Column(name = "net_pay", nullable = false)
    private double netPay;
}
