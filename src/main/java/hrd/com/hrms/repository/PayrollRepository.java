package hrd.com.hrms.repository;

import hrd.com.hrms.model.Payroll;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Repository
public interface PayrollRepository extends JpaRepository<Payroll, UUID> {
    Page<Payroll> findByEmployeeId(UUID employeeId, Pageable pageable);
    Page<Payroll> findByEmployeeDepartmentId(UUID departmentId, Pageable pageable);

    // Guard against generating duplicate payroll for the same employee within a month
    boolean existsByEmployeeIdAndPaymentDateBetween(UUID employeeId, LocalDate start, LocalDate end);

    // Postgres-compatible monthly expenditure aggregation
    @Query(value = "SELECT to_char(p.payment_date, 'YYYY-MM') as month, SUM(p.net_pay) as total_expenditure " +
            "FROM payroll p GROUP BY to_char(p.payment_date, 'YYYY-MM') ORDER BY month", nativeQuery = true)
    List<Map<String, Object>> getMonthlyPayrollExpenditure();
}