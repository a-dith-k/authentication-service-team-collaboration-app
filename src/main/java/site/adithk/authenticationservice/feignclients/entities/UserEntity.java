package site.adithk.authenticationservice.feignclients.entities;

import lombok.Data;
import site.adithk.authenticationservice.enums.UserRole;

@Data
public class UserEntity {


    private Integer id;

    private String email;

    private String password;

    private String firstName;

    private String lastName;

    private Boolean isBlocked;

    private UserRole userRole;
}
