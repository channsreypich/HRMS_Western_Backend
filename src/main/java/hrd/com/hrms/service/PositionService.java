package hrd.com.hrms.service;

import hrd.com.hrms.dto.request.PositionRequest;
import hrd.com.hrms.dto.response.PositionResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.UUID;

public interface PositionService {
    PositionResponse createPosition(PositionRequest request);
    PositionResponse updatePosition(UUID id, PositionRequest request);
    Page<PositionResponse> getAllPositions(Pageable pageable);
    PositionResponse getPositionById(UUID id);
    void deletePosition(UUID id);
}