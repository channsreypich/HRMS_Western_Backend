package hrd.com.hrms.service;

import hrd.com.hrms.dto.request.DepartmentRequest;
import hrd.com.hrms.dto.response.DepartmentResponse;

import java.util.List;
import java.util.UUID;

public interface DepartmentService {
    List<DepartmentResponse> getAllDepartments();
    DepartmentResponse createDepartment(DepartmentRequest request);
    DepartmentResponse updateDepartment(UUID id, DepartmentRequest request);
    void deleteDepartment(UUID id);
}