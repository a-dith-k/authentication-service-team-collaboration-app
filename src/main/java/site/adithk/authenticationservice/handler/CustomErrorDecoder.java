package site.adithk.authenticationservice.handler;

import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import site.adithk.authenticationservice.exceptions.UserAlreadyExistsException;
import site.adithk.authenticationservice.exceptions.UserNotFoundException;

@Component
@Slf4j
public class CustomErrorDecoder implements ErrorDecoder {
    private final ErrorDecoder errorDecoder = new Default();
    @Override
    public Exception decode(String methodKey, Response response) {
        log.info("methodKey:{} reponse:{}",methodKey,response);

        return switch (response.status()) {
            case 409 -> new UserAlreadyExistsException("User already exists");
            case 404 -> new UserNotFoundException(" User Not found");
            default -> errorDecoder.decode(methodKey, response);
        };
    }
}

