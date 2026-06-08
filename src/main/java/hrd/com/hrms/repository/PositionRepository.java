package hrd.com.hrms.repository;

import hrd.com.hrms.model.Position;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface PositionRepository extends JpaRepository<Position, UUID> {
    List<Position> findByDepartmentId(UUID departmentId);
    Page<Position> findByTitleContainingIgnoreCase(String title, Pageable pageable);
}