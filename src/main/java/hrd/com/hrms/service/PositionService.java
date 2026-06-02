package hrd.com.hrms.service;

import hrd.com.hrms.dto.request.PositionRequest;
import hrd.com.hrms.dto.response.PositionResponse;
import java.util.List;
import java.util.UUID;

public interface PositionService {
    PositionResponse createPosition(PositionRequest request);
    PositionResponse getPositionById(UUID id);
    List<PositionResponse> getAllPositions();
    PositionResponse updatePosition(UUID id, PositionRequest request);
    void deletePosition(UUID id);
}
