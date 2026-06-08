package hrd.com.hrms.repository;

import hrd.com.hrms.model.Payroll;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface PayrollRepository extends JpaRepository<Payroll, UUID> {
    Page<Payroll> findByEmployeeId(UUID employeeId, Pageable pageable);
    Page<Payroll> findByEmployeePositionDepartmentId(UUID departmentId, Pageable pageable);
}