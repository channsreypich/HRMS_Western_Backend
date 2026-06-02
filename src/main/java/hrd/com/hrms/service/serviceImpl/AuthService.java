package hrd.com.hrms.service.serviceImpl;

import hrd.com.hrms.dto.request.LoginRequest;
import hrd.com.hrms.dto.request.RegisterRequest;
import hrd.com.hrms.dto.response.AuthResponse;
import hrd.com.hrms.dto.response.RegisterResponse;

public interface AuthService {
    RegisterResponse registerUser(RegisterRequest request);
    AuthResponse login(LoginRequest request);
}