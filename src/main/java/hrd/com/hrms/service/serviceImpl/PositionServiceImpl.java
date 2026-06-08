package hrd.com.hrms.service.serviceImpl;

import hrd.com.hrms.dto.request.PositionRequest;
import hrd.com.hrms.dto.response.PositionResponse;
import hrd.com.hrms.exception.ResourceNotFoundException;
import hrd.com.hrms.mapper.PositionMapper;
import hrd.com.hrms.model.Department;
import hrd.com.hrms.model.Position;
import hrd.com.hrms.repository.DepartmentRepository;
import hrd.com.hrms.repository.PositionRepository;
import hrd.com.hrms.service.PositionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;

@Service
@Transactional
public class PositionServiceImpl implements PositionService {

    private final PositionRepository positionRepository;
    private final DepartmentRepository departmentRepository;
    private final PositionMapper positionMapper;

    public PositionServiceImpl(PositionRepository positionRepository, DepartmentRepository departmentRepository, PositionMapper positionMapper) {
        this.positionRepository = positionRepository;
        this.departmentRepository = departmentRepository;
        this.positionMapper = positionMapper;
    }

    @Override
    public PositionResponse createPosition(PositionRequest request) {
        Department department = null;
        if (request.getDepartmentId() != null) {
            department = departmentRepository.findById(request.getDepartmentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Department linked to position not found"));
        }

        Position position = Position.builder()
                .title(request.getTitle())
                .department(department)
                .build();
        return positionMapper.toResponse(positionRepository.save(position));
    }

    @Override
    public PositionResponse updatePosition(UUID id, PositionRequest request) {
        Position position = positionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Position not found with id: " + id));

        if (request.getDepartmentId() != null) {
            Department department = departmentRepository.findById(request.getDepartmentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Department linked to position not found"));
            position.setDepartment(department);
        } else {
            position.setDepartment(null);
        }

        position.setTitle(request.getTitle());
        return positionMapper.toResponse(positionRepository.save(position));
    }

    @Override
    public Page<PositionResponse> getAllPositions(Pageable pageable) {
        return positionRepository.findAll(pageable).map(positionMapper::toResponse);
    }

    @Override
    public PositionResponse getPositionById(UUID id) {
        return positionRepository.findById(id)
                .map(positionMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Position not found with id: " + id));
    }

    @Override
    public void deletePosition(UUID id) {
        Position position = positionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Position not found with id: " + id));
        positionRepository.delete(position);
    }
}