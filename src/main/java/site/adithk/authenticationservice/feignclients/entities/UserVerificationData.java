package site.adithk.authenticationservice.feignclients.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserVerificationData {


    private Integer id;

    private String verificationLink;

    private Boolean isVerified;

    private LocalDateTime generationTime;
}
