package hrd.com.hrms.controller;

import hrd.com.hrms.common.ApiResponse;
import hrd.com.hrms.dto.request.RegisterRequest;
import hrd.com.hrms.dto.request.StatusUpdateRequest;
import hrd.com.hrms.dto.response.UserResponse;
import hrd.com.hrms.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/users-list")
    public ResponseEntity<ApiResponse<List<UserResponse>>> getUsersList() {
        List<UserResponse> users = userService.getAllUsers();
        return ResponseEntity.ok(ApiResponse.success(users, "User accounts list fetched"));
    }

    @PostMapping("/register-hr")
    public ResponseEntity<ApiResponse<UserResponse>> registerHR(@Valid @RequestBody RegisterRequest request) {
        UserResponse response = userService.registerHR(request);
        return ResponseEntity.ok(ApiResponse.success(response, "HR Operator registered successfully"));
    }

    @PatchMapping("/user-status/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> changeStatus(
            @PathVariable UUID id,
            @Valid @RequestBody StatusUpdateRequest request) {
        UserResponse response = userService.toggleStatus(id, request);
        return ResponseEntity.ok(ApiResponse.success(response, "Operator operational access status modified"));
    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity<ApiResponse<Void>> removeUser(@PathVariable UUID id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Account purged from system database"));
    }
}