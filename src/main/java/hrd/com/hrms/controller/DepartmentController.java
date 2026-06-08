package hrd.com.hrms.controller;

import hrd.com.hrms.common.ApiResponse;
import hrd.com.hrms.dto.request.DepartmentRequest;
import hrd.com.hrms.dto.response.DepartmentResponse;
import hrd.com.hrms.service.DepartmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/departments")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class DepartmentController {

    private final DepartmentService departmentService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<DepartmentResponse>>> fetchAllDepartments() {
        List<DepartmentResponse> list = departmentService.getAllDepartments();
        return ResponseEntity.ok(ApiResponse.success(list, "Departments loaded successfully"));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<DepartmentResponse>> handleCreateDepartment(
            @Valid @RequestBody DepartmentRequest request) {
        DepartmentResponse res = departmentService.createDepartment(request);
        return ResponseEntity.ok(ApiResponse.success(res, "Department created successfully"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<DepartmentResponse>> handleUpdateDepartment(
            @PathVariable UUID id,
            @Valid @RequestBody DepartmentRequest request) {
        DepartmentResponse res = departmentService.updateDepartment(id, request);
        return ResponseEntity.ok(ApiResponse.success(res, "Department updated successfully"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> handleDeleteDepartment(@PathVariable UUID id) {
        departmentService.deleteDepartment(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Department deleted successfully"));
    }
}