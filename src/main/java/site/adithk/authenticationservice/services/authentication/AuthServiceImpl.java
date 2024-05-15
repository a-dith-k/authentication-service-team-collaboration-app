package site.adithk.authenticationservice.services.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import site.adithk.authenticationservice.dtos.LoginRequest;
import site.adithk.authenticationservice.dtos.LoginResponse;
import site.adithk.authenticationservice.dtos.UserRegistrationRequest;
import site.adithk.authenticationservice.dtos.UserRegistrationResponse;
import site.adithk.authenticationservice.feignclients.UserManagementServiceClient;
import site.adithk.authenticationservice.feignclients.entities.UserEntity;
import site.adithk.authenticationservice.services.jwt.JwtService;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private  UserManagementServiceClient userManagementServiceClient;
    private final PasswordEncoder passwordEncoder;
    final private AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;

    private final JwtService jwtService;

    public AuthServiceImpl(PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, UserDetailsService userDetailsService, JwtService jwtService) {

        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtService = jwtService;
    }
    @Override
    public UserRegistrationResponse registerUser(UserRegistrationRequest registrationRequest) {
        registrationRequest
                .setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
       return userManagementServiceClient.saveUser(registrationRequest);
    }

    @Override
    public LoginResponse authenticateUser(LoginRequest loginRequest) {
        UserEntity user = userManagementServiceClient.getUserData(loginRequest.username());
        System.out.println(user);
        boolean passwordMatches = passwordEncoder.matches(loginRequest.password(), user.getPassword());


        Authentication authentication=
                authenticationManager
                        .authenticate(new UsernamePasswordAuthenticationToken(
                                loginRequest.username(),loginRequest.password())
                        );


        UserDetails userDetails= userDetailsService.loadUserByUsername(loginRequest.username());
        String token=
                jwtService.generateToken(userDetails);

        if(authentication.isAuthenticated()&&token!=null)
            return new LoginResponse(loginRequest.username(),token);
        else
            throw new BadCredentialsException("Bad Credentials");


    }

    @Override
    public void validateToken(String token) {

    }
}
