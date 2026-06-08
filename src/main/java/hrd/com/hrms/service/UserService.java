package hrd.com.hrms.service;

import hrd.com.hrms.dto.response.UserResponse;
import java.util.List;

public interface UserService {
    List<UserResponse> getAllUsers();
}