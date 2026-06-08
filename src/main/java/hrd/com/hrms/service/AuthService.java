package hrd.com.hrms.service;

import hrd.com.hrms.dto.request.LoginRequest;
import hrd.com.hrms.dto.response.AuthResponse;

public interface AuthService {
    AuthResponse login(LoginRequest loginRequest);
}