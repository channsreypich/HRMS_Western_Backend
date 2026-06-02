package hrd.com.hrms.service;

import hrd.com.hrms.dto.request.LoginRequest;
import hrd.com.hrms.dto.request.RegisterRequest;
import hrd.com.hrms.dto.response.LoginResponse;
import hrd.com.hrms.dto.response.RegisterResponse;
import jakarta.validation.Valid;

public class UserService {
    public RegisterResponse registerUser(@Valid RegisterRequest request) {
        return null;
    }

    public LoginResponse loginUser(@Valid LoginRequest request) {
        return null;
    }
}
