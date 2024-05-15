package site.adithk.authenticationservice.dtos;


import lombok.Data;

@Data
public class UserDataResponse {

    String email;
    String password;
    String firstName;
    String lastName;
}
