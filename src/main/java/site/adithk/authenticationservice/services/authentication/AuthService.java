package site.adithk.authenticationservice.services.authentication;

import site.adithk.authenticationservice.dtos.LoginRequest;
import site.adithk.authenticationservice.dtos.LoginResponse;
import site.adithk.authenticationservice.dtos.UserRegistrationRequest;
import site.adithk.authenticationservice.dtos.UserRegistrationResponse;
import site.adithk.authenticationservice.exceptions.AlreadyVerifiedException;
import site.adithk.authenticationservice.exceptions.InvalidLinkException;
import site.adithk.authenticationservice.exceptions.UserNotFoundException;
import site.adithk.authenticationservice.exceptions.VerificationLinkExpiredException;

public interface AuthService {

    UserRegistrationResponse registerUser(UserRegistrationRequest registrationRequest);

    LoginResponse authenticateUser(LoginRequest loginRequest);

    void validateToken(String token);

    void resendVerificationLink(String email) throws AlreadyVerifiedException, UserNotFoundException;

    void verifyUserEmail(String verificationString) throws VerificationLinkExpiredException, InvalidLinkException, AlreadyVerifiedException;
}
