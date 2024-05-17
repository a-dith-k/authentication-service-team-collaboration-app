package site.adithk.authenticationservice.dtos;

import lombok.*;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class GenericErrorResponse {

    private HttpStatus status;
    private String errorCode;
    private String errorMessage;

}
