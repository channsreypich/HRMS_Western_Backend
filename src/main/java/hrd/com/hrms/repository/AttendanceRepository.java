package hrd.com.hrms.repository;

import hrd.com.hrms.model.Attendance;
import hrd.com.hrms.model.Employee;
import hrd.com.hrms.enums.AttendanceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, UUID> {
    Optional<Attendance> findByEmployeeAndDate(Employee employee, LocalDate date);

    // Attendance history for a single employee, most recent day first
    List<Attendance> findByEmployeeIdOrderByDateDesc(UUID employeeId);

    // Add this to compute analytics instantly
    long countByDateAndStatus(LocalDate date, AttendanceStatus status);
}