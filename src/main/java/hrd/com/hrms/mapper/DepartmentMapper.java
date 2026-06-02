package hrd.com.hrms.mapper;

import hrd.com.hrms.dto.department.DepartmentRequest;
import hrd.com.hrms.dto.department.DepartmentResponse;
import hrd.com.hrms.model.Department;
import org.springframework.stereotype.Component;

@Component
public class DepartmentMapper {

    public Department toEntity(DepartmentRequest request) {
        if (request == null) return null;

        Department department = new Department();
        department.setName(request.getName());
        department.setDescription(request.getDescription());
        return department;
    }

    public DepartmentResponse toResponse(Department department) {
        if (department == null) return null;

        return new DepartmentResponse(
                department.getId(),
                department.getName(),
                department.getDescription()
        );
    }
}