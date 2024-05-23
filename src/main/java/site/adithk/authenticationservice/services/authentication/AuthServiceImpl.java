package site.adithk.authenticationservice.services.authentication;

import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import site.adithk.authenticationservice.dtos.*;
import site.adithk.authenticationservice.exceptions.AlreadyVerifiedException;
import site.adithk.authenticationservice.exceptions.InvalidLinkException;
import site.adithk.authenticationservice.exceptions.UserNotFoundException;
import site.adithk.authenticationservice.exceptions.VerificationLinkExpiredException;
import site.adithk.authenticationservice.feignclients.UserManagementServiceClient;
import site.adithk.authenticationservice.feignclients.entities.UserEntity;
import site.adithk.authenticationservice.feignclients.entities.UserVerificationData;
import site.adithk.authenticationservice.helper.LinkGenerator;
import site.adithk.authenticationservice.services.emailsender.EmailSenderService;
import site.adithk.authenticationservice.services.jwt.JwtService;

import java.time.LocalDateTime;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService {

    @Autowired
    private  UserManagementServiceClient userManagementServiceClient;
    private final PasswordEncoder passwordEncoder;
    final private AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;
    private final EmailSenderService emailSenderService;
    private final String domainUrl;
    private final ModelMapper modelMapper;

    public AuthServiceImpl(PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, UserDetailsService userDetailsService, JwtService jwtService, EmailSenderService emailSenderService, @Value("${app.frontend.domain.name}") String domainUrl, ModelMapper modelMapper) {

        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtService = jwtService;
        this.emailSenderService = emailSenderService;
        this.domainUrl = domainUrl;
        this.modelMapper = modelMapper;
    }
    @Override
    public UserRegistrationResponse registerUser(UserRegistrationRequest registrationRequest) {


        StringBuilder verificationString=LinkGenerator.getRandomString(78);

        registrationRequest
                .setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
        registrationRequest
                .setVerificationLink(verificationString.toString());


        ResponseEntity<UserRegistrationResponse> response = userManagementServiceClient.saveUser(registrationRequest);
        emailSenderService
                .sendSimpleEmail(
                        registrationRequest.getEmail(),
                        "One App Verification Link",
                        domainUrl.concat(verificationString.toString())
                );
        return response.getBody();
    }

    @Override
    public LoginResponse authenticateUser(LoginRequest loginRequest) {


        //Obtaining Authentication Object
        Authentication authentication=
                authenticationManager
                        .authenticate(new UsernamePasswordAuthenticationToken(
                                loginRequest.username(),loginRequest.password())
                        );


        UserDetails userDetails=
                userDetailsService.loadUserByUsername(loginRequest.username());

        log.info("UserManagementService[UserData]:{}",userDetails);

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

    @Override
    public void verifyUserEmail(String verificationString) throws
            VerificationLinkExpiredException, AlreadyVerifiedException, InvalidLinkException {


        try{
            ResponseEntity<UserDataResponse> userDataResponseEntity=userManagementServiceClient.getUserDataByVerificationString(verificationString);
            UserDataResponse userData=userDataResponseEntity.getBody();
            if(userData!=null){
                if (userData.getVerificationData().getGenerationTime().plusDays(2).isBefore(LocalDateTime.now()))
                    throw new VerificationLinkExpiredException("verification link is expired");
                else if (userData.getVerificationData().getIsVerified())
                    throw new AlreadyVerifiedException("User Already Verified");
                else{
                    userManagementServiceClient.updateUserPartially(userData.getEmail(),true);
                }

            }

        }catch (VerificationLinkExpiredException ex){
            log.info("Exception In Verification:{}",ex.getMessage());
            throw new VerificationLinkExpiredException("Verification link is expired");
        }catch (AlreadyVerifiedException ex){
            log.info("Exception In Verification:{}",ex.getMessage());
            throw new  AlreadyVerifiedException("already verified");
        }catch (Exception ex){
            log.info("general exception caught while verifying link throwing invalid link exception");
            log.info("Exception In Verification:{}",ex.getMessage());
            throw new InvalidLinkException("Invalid Verification Link");
        }



    }

    @Override
    public void resendVerificationLink(String email) throws AlreadyVerifiedException, UserNotFoundException {
        ResponseEntity<UserDataResponse> userData = userManagementServiceClient.getUserData(email);
        UserDataResponse response=null;
        if(userData.getBody()!=null)
            response=userData.getBody();

        if (response.getVerificationData().getIsVerified())
            throw new AlreadyVerifiedException("User Already Verified");


        StringBuilder newVerificationLink = LinkGenerator.getRandomString(78);
        emailSenderService
                .sendSimpleEmail(email, "One App Verification Link", domainUrl.concat(newVerificationLink.toString()));


        UserVerificationData newVerificationData
                =UserVerificationData.builder()
                .verificationLink(newVerificationLink.toString())
                .generationTime(LocalDateTime.now())
                .isVerified(false)
                .build();

        userManagementServiceClient
                .updateVerificationData(
                        email,modelMapper
                                .map(newVerificationData,UserVerificationDataUpdateRequest.class)
                );
    }
}
