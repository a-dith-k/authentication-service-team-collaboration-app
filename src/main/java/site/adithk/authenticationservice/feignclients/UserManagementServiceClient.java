package site.adithk.authenticationservice.feignclients;


import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.w3c.dom.UserDataHandler;
import site.adithk.authenticationservice.dtos.*;
import site.adithk.authenticationservice.feignclients.entities.UserEntity;

@FeignClient(value = "user-management-service")
public interface UserManagementServiceClient {

    @PostMapping("users")
    ResponseEntity<UserRegistrationResponse> saveUser(@Valid @RequestBody UserRegistrationRequest registrationRequest);

    @GetMapping("users/{email}")
    ResponseEntity<UserDataResponse> getUserData(@PathVariable("email") String email);

    @GetMapping("users/by-verification-link")
    ResponseEntity<UserDataResponse> getUserDataByVerificationString(@RequestParam("verification-string") String verificationString);

    @PutMapping("users/update-by-fields")
    ResponseEntity<Void> updateUserPartially(@RequestParam("email") String email,@RequestParam(value = "is-verified")Boolean isVerified);

    @PutMapping("verification-data")
    ResponseEntity<Void> updateVerificationData(@RequestParam("email") String email,@RequestBody UserVerificationDataUpdateRequest request);

    @PutMapping("enable/{email}")
    ResponseEntity<Void> enableUser(@PathVariable("email") String email);

}
