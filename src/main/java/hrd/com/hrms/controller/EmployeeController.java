package hrd.com.hrms.controller;

import hrd.com.hrms.dto.request.EmployeeRequest;
import hrd.com.hrms.dto.response.EmployeeResponse;
import hrd.com.hrms.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeController {
    private final EmployeeService employeeService;

    @PostMapping
    public ResponseEntity<EmployeeResponse> create(@Valid @RequestBody EmployeeRequest r) { return ResponseEntity.ok(employeeService.createEmployee(r)); }

    @PutMapping("/{id}")
    public ResponseEntity<EmployeeResponse> update(@PathVariable UUID id, @Valid @RequestBody EmployeeRequest r) { return ResponseEntity.ok(employeeService.updateEmployee(id, r)); }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeResponse> getById(@PathVariable UUID id) { return ResponseEntity.ok(employeeService.getEmployeeById(id)); }

    @GetMapping
    public ResponseEntity<List<EmployeeResponse>> getAll() { return ResponseEntity.ok(employeeService.getAllEmployees()); }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) { employeeService.deleteEmployee(id); return ResponseEntity.noContent().build(); }
}