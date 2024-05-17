package site.adithk.authenticationservice.exceptionhandlers;


import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import site.adithk.authenticationservice.dtos.VerificationResponse;
import site.adithk.authenticationservice.exceptions.*;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class UserExceptionHandler {


//    @ExceptionHandler( FeignException.class)
//    public  ResponseEntity<Object>  handleFeignException(FeignException ex){
//
//        if(ex.status()==404)
//            return new ResponseEntity<>("User Not Found", HttpStatus.NOT_FOUND);
//        if(ex.status()==409)
//            return new ResponseEntity<>("User AlreadyExists", HttpStatus.CONFLICT);
//
//         return new ResponseEntity<>("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
//    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<String> userExists(UserAlreadyExistsException ex) {

        return new ResponseEntity<>("User AlreadyExists", HttpStatus.CONFLICT);
    }

    @ExceptionHandler(InvalidLinkException.class)
    public ResponseEntity<VerificationResponse> invalidVerificationLink(InvalidLinkException ex) {

        return new ResponseEntity<>(
                VerificationResponse
                        .builder()
                        .statusCode(HttpStatus.BAD_REQUEST)
                        .error("invalid")
                        .message(ex.getMessage())
                        .build(),
                HttpStatus.BAD_REQUEST);
    }




    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,String>> requestBodyInvalid(MethodArgumentNotValidException ex) {
        log.info("ex{}",ex.getMessage());
        Map<String,String>errorMap=new HashMap<String,String>();

        ex.getBindingResult().getAllErrors().forEach(error->{
            String inputField=((FieldError)error).getField();
            String errorMessage=error.getDefaultMessage();
            errorMap.put(inputField,errorMessage);
        });

        return new ResponseEntity<>(errorMap, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(UserNotVerifiedException.class)
    public ResponseEntity<String> userNotVerified(UserNotVerifiedException ex) {

        return new ResponseEntity<>("user is unverified", HttpStatus.LOCKED);
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<String> userDisabled(DisabledException ex) {

        return new ResponseEntity<>("user is disabled", HttpStatus.LOCKED);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<String> badCredentials(BadCredentialsException ex) {

        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(InternalAuthenticationServiceException.class)
    public ResponseEntity<String> userNotFound(InternalAuthenticationServiceException ex) {

        return new ResponseEntity<>("User Not Found", HttpStatus.NOT_FOUND);
    }




    @ExceptionHandler(VerificationLinkExpiredException.class)
    public ResponseEntity<VerificationResponse> verificationLinkExpired(VerificationLinkExpiredException ex) {

        return new ResponseEntity<>(
                VerificationResponse
                        .builder()
                        .statusCode(HttpStatus.BAD_REQUEST)
                        .error("expired")
                        .message(ex.getMessage())
                        .build(),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AlreadyVerifiedException.class)
    public ResponseEntity<VerificationResponse> alreadyVerifiedException(AlreadyVerifiedException ex) {

        return new ResponseEntity<>(
                VerificationResponse
                        .builder()
                        .statusCode(HttpStatus.BAD_REQUEST)
                        .error("verified")
                        .message(ex.getMessage())
                        .build(),
                HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> genericError(Exception ex) {

    return new ResponseEntity<>("Unable to process the request right now",HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
