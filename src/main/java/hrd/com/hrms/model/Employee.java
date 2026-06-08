package hrd.com.hrms.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

@Entity
@Table(name = "employees")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Column(name = "first_name", length = 100)
    private String firstName;

    @Column(name = "last_name", length = 100)
    private String lastName;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "department_id")
    private Department department;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "position_id")
    private Position position;
    @Column(name = "employee_code", unique = true, length = 50)
    private String employeeCode;
    @Column(name = "phone", length = 20)
    private String phone;

    @Column(name = "status", length = 20)
    private String status; // e.g., "active", "inactive"

    @Column(name = "hire_date")
    private java.time.LocalDate hireDate;

    @Column(name = "base_salary", precision = 10, scale = 2)
    private java.math.BigDecimal baseSalary;
}