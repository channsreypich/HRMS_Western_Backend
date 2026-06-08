package hrd.com.hrms.repository;

import hrd.com.hrms.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, UUID> {
    Optional<Department> findByCode(String code);
    boolean existsByCode(String code);
    boolean existsByName(String name);
}