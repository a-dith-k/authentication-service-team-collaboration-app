package site.adithk.authenticationservice.services.emailsender;

public interface EmailSenderService {

    void sendSimpleEmail( String toEmail, String subject, String body);
}
