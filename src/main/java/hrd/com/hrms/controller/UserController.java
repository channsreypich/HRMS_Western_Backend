package hrd.com.hrms.controller;

import hrd.com.hrms.common.ApiResponse;
import hrd.com.hrms.model.User;
import hrd.com.hrms.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;

    @GetMapping
    public ResponseEntity<ApiResponse<List<User>>> getAllUsers() {
        List<User> users = userRepository.findAll();
        return ResponseEntity.ok(new ApiResponse<>("Users list successfully aggregated", users, true, java.time.LocalDateTime.now()));
    }

    @PatchMapping("/{id}/toggle-status")
    public ResponseEntity<ApiResponse<User>> toggleUserStatus(@PathVariable UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Target user resource reference missing"));
        user.setIsActive(!user.getIsActive());
        User updatedUser = userRepository.save(user);
        return ResponseEntity.ok(new ApiResponse<>("User availability status updated contextually", updatedUser, true, java.time.LocalDateTime.now()));
    }
}