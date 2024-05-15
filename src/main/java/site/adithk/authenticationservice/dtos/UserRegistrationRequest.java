package site.adithk.authenticationservice.dtos;

import jakarta.validation.constraints.*;
import lombok.Getter;

@Getter
public class UserRegistrationRequest {

    @NotBlank
    @NotNull
    @Size(min = 4) String firstName;
    @NotNull String lastName;
    @NotNull
    @NotBlank
    @Email String email;

    public void setPassword(String password) {
        this.password = password;
    }

    @NotBlank
    @NotNull
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*])[a-zA-Z0-9!@#$%^&*]{8,12}$",
            message = "password must be min 4 and max 12 length containing at" +
                    " least 1 uppercase, 1 lowercase, 1 special character and 1 digit "
    )
    String password;
}