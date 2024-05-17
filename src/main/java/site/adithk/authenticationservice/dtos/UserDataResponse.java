package site.adithk.authenticationservice.dtos;


import lombok.Data;
import site.adithk.authenticationservice.enums.UserRole;
import site.adithk.authenticationservice.feignclients.entities.UserVerificationData;


@Data
public class UserDataResponse {
    private Integer id;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private Boolean isBlocked;
    private UserRole userRole;
    private UserVerificationData verificationData;
}
