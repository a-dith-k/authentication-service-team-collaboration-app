package site.adithk.authenticationservice.exceptions;

public class VerificationLinkExpiredException extends RuntimeException {
    public VerificationLinkExpiredException(String message) {
        super(message);
    }
}
