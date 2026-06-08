package hrd.com.hrms.repository;

import hrd.com.hrms.enums.LeaveStatus;
import hrd.com.hrms.model.Leave;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface LeaveRepository extends JpaRepository<Leave, UUID> {
    Page<Leave> findByEmployeeId(UUID employeeId, Pageable pageable);
    List<Leave> findByEmployeeId(UUID employeeId);

    Page<Leave> findByStatus(LeaveStatus status, Pageable pageable);
    long countByStatus(LeaveStatus status);
}