package hrd.com.hrms.mapper;

import hrd.com.hrms.dto.response.PositionResponse;
import hrd.com.hrms.model.Position;
import org.springframework.stereotype.Component;

@Component
public class PositionMapper {
    public PositionResponse toResponse(Position position) {
        if (position == null) return null;
        return PositionResponse.builder()
                .id(position.getId())
                .title(position.getTitle())
                .baseSalary(position.getBaseSalary())
                .departmentId(position.getDepartment() != null ? position.getDepartment().getId() : null)
                .departmentName(position.getDepartment() != null ? position.getDepartment().getName() : "N/A")
                .build();
    }
}