package hrd.com.hrms.mapper;

import hrd.com.hrms.dto.response.DepartmentResponse;
import hrd.com.hrms.model.Department;
import org.springframework.stereotype.Component;

@Component
public class DepartmentMapper {
    public DepartmentResponse toResponse(Department department) {
        if (department == null) return null;
        return DepartmentResponse.builder()
                .id(department.getId())
                .code(department.getCode())
                .name(department.getName())
                .build();
    }
}