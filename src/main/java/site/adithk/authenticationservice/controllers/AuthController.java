package site.adithk.authenticationservice.controllers;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import site.adithk.authenticationservice.dtos.*;
import site.adithk.authenticationservice.exceptions.AlreadyVerifiedException;
import site.adithk.authenticationservice.exceptions.InvalidLinkException;
import site.adithk.authenticationservice.exceptions.UserNotFoundException;
import site.adithk.authenticationservice.exceptions.VerificationLinkExpiredException;
import site.adithk.authenticationservice.services.authentication.AuthService;

@RestController
@RequestMapping("auth")
@Slf4j
public class AuthController {

   private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("register")
    public ResponseEntity<UserRegistrationResponse> registerUser (@Valid @RequestBody UserRegistrationRequest registrationRequest) {
        return new ResponseEntity<>(authService.registerUser(registrationRequest), HttpStatus.CREATED);
    }

    @PostMapping("verify")
    public ResponseEntity<VerificationResponse> verifyUserEmail(@RequestParam("verificationString")String verificationString) throws InvalidLinkException, VerificationLinkExpiredException, AlreadyVerifiedException {
        log.info("VerificationLink in Request:{}",verificationString);
        authService.verifyUserEmail(verificationString);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @PostMapping("authenticate")
    public ResponseEntity<LoginResponse> authenticateUser(@RequestBody LoginRequest loginRequest){
      return new ResponseEntity<>(authService.authenticateUser(loginRequest), HttpStatus.OK);

    }

    @PostMapping("resend")
    public ResponseEntity<Void> resendEmail(@RequestParam("email")String email) throws UserNotFoundException, AlreadyVerifiedException {

        authService.resendVerificationLink(email);
        return new ResponseEntity<>(HttpStatus.OK);
    }




}
