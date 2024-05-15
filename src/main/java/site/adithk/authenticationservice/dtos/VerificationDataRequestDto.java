package site.adithk.authenticationservice.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VerificationDataRequestDto {
	String email;
	String verificationLink;
}
