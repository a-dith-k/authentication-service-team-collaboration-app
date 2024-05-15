package site.adithk.authenticationservice.exceptions;

public class VerificationLinkExpiredException extends Throwable {
    public VerificationLinkExpiredException(String message) {
        super(message);
    }
}
