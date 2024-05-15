package site.adithk.authenticationservice.feignclients;


import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import site.adithk.authenticationservice.dtos.UserDataResponse;
import site.adithk.authenticationservice.dtos.UserRegistrationRequest;
import site.adithk.authenticationservice.dtos.UserRegistrationResponse;
import site.adithk.authenticationservice.feignclients.entities.UserEntity;

@FeignClient(value = "user-management-service")
public interface UserManagementServiceClient {

    @PostMapping("users")
    UserRegistrationResponse saveUser(@Valid @RequestBody UserRegistrationRequest registrationRequest);

    @GetMapping("users")
    UserEntity getUserData(@RequestParam("email") String email);

}
