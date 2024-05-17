package site.adithk.authenticationservice.exceptionhandlers;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import site.adithk.authenticationservice.exceptions.UserAlreadyExistsException;

public class CustomErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {
        if (response.status() == HttpStatus.CONFLICT.value()) {
            return new UserAlreadyExistsException("User already exists");
        }

        return new ResponseStatusException(HttpStatus.valueOf(response.status()), response.reason());
    }
}
