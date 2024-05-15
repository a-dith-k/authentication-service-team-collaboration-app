package site.adithk.authenticationservice.handler;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.DisabledException;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import site.adithk.authenticationservice.exceptions.*;

@ControllerAdvice
public class UserExceptionHandler {

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<String> userAlreadyExists(UserAlreadyExistsException ex) {

        return new ResponseEntity<>("User AlreadyExists", HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> userNotFound(UserNotFoundException ex) {

        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<String> userNotFound(DisabledException ex) {

        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }



//    @ExceptionHandler(VerificationLinkExpiredException.class)
//    public ResponseEntity<VerificationResponse> verificationLinkExpired(VerificationLinkExpiredException ex) {
//
//        return new ResponseEntity<>(
//                VerificationResponse
//                        .builder()
//                        .statusCode(HttpStatus.BAD_REQUEST)
//                        .error("expired")
//                        .message(ex.getMessage())
//                        .build(),
//                HttpStatus.BAD_REQUEST);
//    }

//    @ExceptionHandler(AlreadyVerifiedException.class)
//    public ResponseEntity<VerificationResponse> alreadyVerifiedException(AlreadyVerifiedException ex) {
//
//        return new ResponseEntity<>(
//                VerificationResponse
//                        .builder()
//                        .statusCode(HttpStatus.BAD_REQUEST)
//                        .error("verified")
//                        .message(ex.getMessage())
//                        .build(),
//                HttpStatus.BAD_REQUEST);
//    }

//    @ExceptionHandler(InvalidLinkException.class)
//    public ResponseEntity<VerificationResponse> invalidVerficationLink(InvalidLinkException ex) {
//
//        return new ResponseEntity<>(
//                VerificationResponse
//                        .builder()
//                        .statusCode(HttpStatus.BAD_REQUEST)
//                        .error("invalid")
//                        .message(ex.getMessage())
//                        .build(),
//                HttpStatus.BAD_REQUEST);
    //    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> genericError(Exception ex) {

    return new ResponseEntity<>("genericError",HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
