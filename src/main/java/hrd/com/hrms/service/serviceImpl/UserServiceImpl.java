package hrd.com.hrms.service.serviceImpl;

import hrd.com.hrms.dto.response.UserResponse;
import hrd.com.hrms.repository.UserRepository;
import hrd.com.hrms.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> new UserResponse(
                        user.getId(),
                        user.getName(),
                        user.getEmail(),
                        user.getRole() != null ? user.getRole().getName().name() : "USER", // Safe Enum extraction
                        user.isActive()
                ))
                .toList();
    }
}