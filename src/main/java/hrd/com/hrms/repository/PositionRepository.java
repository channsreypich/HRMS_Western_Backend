package hrd.com.hrms.repository;

import hrd.com.hrms.model.Position;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface PositionRepository extends JpaRepository<Position, UUID> {
}