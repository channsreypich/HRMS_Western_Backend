package hrd.com.hrms.service.serviceImpl;

import hrd.com.hrms.dto.request.PositionRequest;
import hrd.com.hrms.dto.response.PositionResponse;
import hrd.com.hrms.model.Position;
import hrd.com.hrms.repository.PositionRepository;
import hrd.com.hrms.service.PositionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PositionServiceImpl implements PositionService {

    private final PositionRepository positionRepository;

    @Override
    @Transactional
    public PositionResponse createPosition(PositionRequest request) {
        Position position = new Position();
        position.setId(UUID.randomUUID());
        position.setTitle(request.getTitle());
        position.setDepartmentId(request.getDepartmentId());

        Position saved = positionRepository.save(position);
        return mapToResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public PositionResponse getPositionById(UUID id) {
        Position position = positionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Position not found with ID: " + id));
        return mapToResponse(position);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PositionResponse> getAllPositions() {
        return positionRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public PositionResponse updatePosition(UUID id, PositionRequest request) {
        Position position = positionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Position not found with ID: " + id));

        position.setTitle(request.getTitle());
        position.setDepartmentId(request.getDepartmentId());

        Position updated = positionRepository.save(position);
        return mapToResponse(updated);
    }

    @Override
    @Transactional
    public void deletePosition(UUID id) {
        if (!positionRepository.existsById(id)) {
            throw new RuntimeException("Position not found with ID: " + id);
        }
        positionRepository.deleteById(id);
    }

    private PositionResponse mapToResponse(Position position) {
        PositionResponse response = new PositionResponse();
        response.setId(position.getId());
        response.setTitle(position.getTitle());
        response.setDepartmentId(position.getDepartmentId());
        return response;
    }
}