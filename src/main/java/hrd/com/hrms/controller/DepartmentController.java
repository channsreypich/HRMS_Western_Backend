package hrd.com.hrms.controller;

import hrd.com.hrms.common.ApiResponse;
import hrd.com.hrms.dto.request.DepartmentRequest;
import hrd.com.hrms.dto.response.DepartmentResponse;
import hrd.com.hrms.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/departments")
@RequiredArgsConstructor
public class DepartmentController {

    private final DepartmentService departmentService;

    @PostMapping
    public ResponseEntity<ApiResponse<DepartmentResponse>> createDepartment(@Valid @RequestBody DepartmentRequest request) {
        DepartmentResponse response = departmentService.createDepartment(request);
        return new ResponseEntity<>(new ApiResponse<>("Department created successfully", response, true, java.time.LocalDateTime.now()), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<DepartmentResponse>> getDepartmentById(@PathVariable UUID id) {
        DepartmentResponse response = departmentService.getDepartmentById(id);
        return ResponseEntity.ok(new ApiResponse<>("Department fetched successfully", response, true, java.time.LocalDateTime.now()));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<DepartmentResponse>>> getAllDepartments() {
        List<DepartmentResponse> response = departmentService.getAllDepartments();
        return ResponseEntity.ok(new ApiResponse<>("All departments fetched successfully", response, true, java.time.LocalDateTime.now()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<DepartmentResponse>> updateDepartment(@PathVariable UUID id, @Valid @RequestBody DepartmentRequest request) {
        DepartmentResponse response = departmentService.updateDepartment(id, request);
        return ResponseEntity.ok(new ApiResponse<>("Department updated successfully", response, true, java.time.LocalDateTime.now()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteDepartment(@PathVariable UUID id) {
        departmentService.deleteDepartment(id);
        return ResponseEntity.ok(new ApiResponse<>("Department deleted successfully", null, true, java.time.LocalDateTime.now()));
    }
}