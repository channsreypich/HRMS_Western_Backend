package hrd.com.hrms.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "attendance")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "UUID", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @Column(name = "clock_in", nullable = false)
    private LocalDateTime clockIn;

    @Column(name = "clock_out")
    private LocalDateTime clockOut;

    @Column(nullable = false, length = 30)
    private String status;

    public void setEmployeeId(@NotNull(message = "Employee ID is required") UUID employeeId) {
    }

    public void setClockInTime(LocalDateTime now) {
    }

    public void setClockOutTime(LocalDateTime now) {
    }

    public UUID getEmployeeId() {
        return null;
    }

    public Object getClockInTime() {
        return null;
    }

    public Object getClockOutTime() {
        return null;
    }
}