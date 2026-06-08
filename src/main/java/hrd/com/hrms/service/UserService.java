package hrd.com.hrms.service;

import hrd.com.hrms.dto.request.RegisterRequest;
import hrd.com.hrms.dto.request.StatusUpdateRequest;
import hrd.com.hrms.dto.response.UserResponse;
import java.util.List;
import java.util.UUID;

public interface UserService {
    List<UserResponse> getAllUsers();
    UserResponse registerHR(RegisterRequest request);
    UserResponse toggleStatus(UUID id, StatusUpdateRequest request);
    void deleteUser(UUID id);
}