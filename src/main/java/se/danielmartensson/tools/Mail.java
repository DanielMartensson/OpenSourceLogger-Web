package se.danielmartensson.tools;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
/**
 * This class is for sending mail to the user
 * @author dell
 *
 */
public class Mail {
	
	@Autowired
    private JavaMailSender javaMailSender;

	public void sendEmail(String email, String subject, String message) {

        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(email);

        msg.setSubject(subject);
        msg.setText(message);

        javaMailSender.send(msg);
    }
}
