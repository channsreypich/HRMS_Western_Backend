package hrd.com.hrms.repository;

import hrd.com.hrms.model.Attendance;
import hrd.com.hrms.model.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, UUID> {
    Optional<Attendance> findByEmployeeIdAndDate(UUID employeeId, LocalDate date);
    Optional<Attendance> findByEmployeeAndDate(Employee employee, LocalDate date);
    Page<Attendance> findByEmployeeId(UUID employeeId, Pageable pageable);
    Page<Attendance> findByDate(LocalDate date, Pageable pageable);
    long countByDateAndCheckInIsNotNull(LocalDate date);
}
