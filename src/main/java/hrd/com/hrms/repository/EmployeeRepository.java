package hrd.com.hrms.repository;

import hrd.com.hrms.model.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, UUID> {
    Optional<Employee> findByUserId(UUID userId);
    Optional<Employee> findByEmployeeCode(String employeeCode);
    Optional<Employee> findByEmployeeCodeIgnoreCase(String employeeCode);

    @Query("SELECT e FROM Employee e WHERE " +
            "LOWER(e.firstName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(e.lastName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(e.user.email) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Employee> searchEmployees(@Param("keyword") String keyword, Pageable pageable);

    Page<Employee> findByPositionDepartmentId(UUID departmentId, Pageable pageable);
    @Query("SELECT d.name as departmentName, COUNT(e) as employeeCount " +
            "FROM Employee e JOIN e.department d GROUP BY d.name")
    List<Map<String, Object>> countEmployeesByDepartment();
}