package hrd.com.hrms.service.serviceImpl;

import hrd.com.hrms.dto.request.LoginRequest;
import hrd.com.hrms.dto.request.RegisterRequest;
import hrd.com.hrms.dto.response.LoginResponse;
import hrd.com.hrms.dto.response.RegisterResponse;
import hrd.com.hrms.model.Role;
import hrd.com.hrms.model.User;
import hrd.com.hrms.repository.RoleRepository;
import hrd.com.hrms.repository.UserRepository;
import hrd.com.hrms.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl extends UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    // Inject BCryptPasswordEncoder and JwtTokenProvider here when Phase 1 JWT Security config is wired up

    @Override
    public RegisterResponse registerUser(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username is already taken!");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email is already in use!");
        }

        Role role = roleRepository.findById(request.getRoleId())
                .orElseThrow(() -> new RuntimeException("Role not found"));

        User user = new User();
        user.setId(UUID.randomUUID());
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword()); // Raw text fallback. Wrap with passwordEncoder.encode() later
        user.setIsActive(true);
        user.setRoleId(role.getId());

        User savedUser = userRepository.save(user);

        RegisterResponse response = new RegisterResponse();
        response.setId(savedUser.getId());
        response.setUsername(savedUser.getUsername());
        response.setEmail(savedUser.getEmail());
        response.setIsActive(savedUser.getIsActive());
        response.setRoleId(savedUser.getRoleId());
        response.setMessage("User registered successfully");
        return response;
    }

    @Override
    public LoginResponse loginUser(LoginRequest request) {
        User user = (User) userRepository.findByUsernameOrEmail(request.getUsernameOrEmail(), request.getUsernameOrEmail())
                .orElseThrow(() -> new BadCredentialsException("Invalid username or password"));

        // Match raw logic or passwordEncoder.matches(request.getPassword(), user.getPassword())
        if (!request.getPassword().equals(user.getPassword())) {
            throw new BadCredentialsException("Invalid username or password");
        }

        Role role = roleRepository.findById(user.getRoleId())
                .orElseThrow(() -> new RuntimeException("User role matching error"));

        LoginResponse response = new LoginResponse();
        response.setToken("mock-jwt-token-string"); // Swap with tokenProvider.generateToken(...)
        response.setUserId(user.getId());
        response.setUsername(user.getUsername());
        response.setRoleName(role.getName());
        return response;
    }
}
