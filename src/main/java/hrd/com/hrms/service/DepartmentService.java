package hrd.com.hrms.service;

import hrd.com.hrms.dto.request.DepartmentRequest;
import hrd.com.hrms.dto.response.DepartmentResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.UUID;

public interface DepartmentService {
    DepartmentResponse createDepartment(DepartmentRequest request);
    DepartmentResponse updateDepartment(UUID id, DepartmentRequest request);
    Page<DepartmentResponse> getAllDepartments(Pageable pageable);
    DepartmentResponse getDepartmentById(UUID id);
    void deleteDepartment(UUID id);
}