package site.adithk.authenticationservice.services.authentication;

import site.adithk.authenticationservice.dtos.LoginRequest;
import site.adithk.authenticationservice.dtos.LoginResponse;
import site.adithk.authenticationservice.dtos.UserRegistrationRequest;
import site.adithk.authenticationservice.dtos.UserRegistrationResponse;

public interface AuthService {

    UserRegistrationResponse registerUser(UserRegistrationRequest registrationRequest);

    LoginResponse authenticateUser(LoginRequest loginRequest);

    void validateToken(String token);

}
