package site.adithk.authenticationservice.controllers;

import jakarta.validation.Valid;
import lombok.extern.java.Log;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import site.adithk.authenticationservice.dtos.LoginRequest;
import site.adithk.authenticationservice.dtos.LoginResponse;
import site.adithk.authenticationservice.dtos.UserRegistrationRequest;
import site.adithk.authenticationservice.dtos.UserRegistrationResponse;
import site.adithk.authenticationservice.feignclients.entities.UserEntity;
import site.adithk.authenticationservice.services.authentication.AuthService;

@RestController
@RequestMapping("auth")
@CrossOrigin(origins = "*")
public class AuthController {

   private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("register")
    public ResponseEntity<UserRegistrationResponse> registerUser (@Valid @RequestBody UserRegistrationRequest registrationRequest) {
        return new ResponseEntity<>(authService.registerUser(registrationRequest), HttpStatus.CREATED);
    }

    @PostMapping("login")
    public ResponseEntity<LoginResponse> authenticateUser(@RequestBody LoginRequest loginRequest){

        return new ResponseEntity<>(authService.authenticateUser(loginRequest), HttpStatus.OK);


    }

}
