package hrd.com.hrms.service.serviceImpl;

import hrd.com.hrms.dto.request.RegisterRequest;
import hrd.com.hrms.dto.request.StatusUpdateRequest;
import hrd.com.hrms.dto.response.UserResponse;
import hrd.com.hrms.exception.ResourceNotFoundException;
import hrd.com.hrms.model.User;
import hrd.com.hrms.model.Role;
import hrd.com.hrms.enums.RoleName;
import hrd.com.hrms.repository.UserRepository;
import hrd.com.hrms.repository.RoleRepository;
import hrd.com.hrms.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::mapToUserResponse)
                .toList();
    }

    @Override
    @Transactional
    public UserResponse registerHR(RegisterRequest request) {
        // Resolve the requested role (defaults to HR when none supplied)
        RoleName roleName = resolveRoleName(request.role());
        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new ResourceNotFoundException("Role " + roleName + " not found in initialization setup"));

        User newUser = User.builder()
                .firstName(request.first_name())
                .lastName(request.last_name())
                .username(request.username())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .role(role)
                .isActive(true)
                .build();

        User savedUser = userRepository.save(newUser);
        return mapToUserResponse(savedUser);
    }

    private RoleName resolveRoleName(String role) {
        if (role == null || role.isBlank()) {
            return RoleName.ROLE_HR;
        }
        String normalized = role.trim().toUpperCase();
        if (normalized.contains("ADMIN")) {
            return RoleName.ROLE_ADMIN;
        }
        if (normalized.contains("EMPLOYEE")) {
            return RoleName.ROLE_EMPLOYEE;
        }
        return RoleName.ROLE_HR;
    }

    @Override
    @Transactional
    public UserResponse toggleStatus(UUID id, StatusUpdateRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User accounts profile not found"));

        // Match frontend values ("Active" / "Inactive")
        boolean activeFlag = "Active".equalsIgnoreCase(request.status());
        user.setActive(activeFlag);

        return mapToUserResponse(userRepository.save(user));
    }

    @Override
    @Transactional
    public void deleteUser(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User account target doesn't exist"));
        userRepository.delete(user);
    }

    private UserResponse mapToUserResponse(User user) {
        String cleanRole = "HR"; // Default fallback
        if (user.getRole() != null) {
            String rawRole = user.getRole().getName().name(); // e.g., "ROLE_ADMIN" or "ROLE_HR"
            if ("ROLE_ADMIN".equals(rawRole)) {
                cleanRole = "Admin";
            } else if ("ROLE_HR".equals(rawRole)) {
                cleanRole = "HR";
            } else {
                cleanRole = "Employee";
            }
        }

        return new UserResponse(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                cleanRole, // Sends clean "Admin" or "HR" string to Vue
                user.isActive() ? "Active" : "Inactive",
                user.getCreatedAt()
        );
    }
}