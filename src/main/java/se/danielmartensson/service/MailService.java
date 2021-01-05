package se.danielmartensson.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import se.danielmartensson.entities.Alarm;

@Service
@PropertySource("classpath:application.properties")
public class MailService {
	
	@Autowired
	private JavaMailSender javaMailSender;
	
    @Value("${service.MailService.subject}")
    private String subject;
    
    @Value("${service.MailService.from}")
    private String from;
    
    @Autowired
    private AlarmService alarmService;
    
	public MailService() {
		
	}

	public void sendMessage(Alarm alarm, String email, String cause, String message) {
		if (alarm.isMessageHasBeenSent())
			return;
		
		// Save the mark inside the database
		alarm.setMessageHasBeenSent(true);
		try {
			alarmService.save(alarm);
		}catch(Exception e) {
			// Does not matter, we still have issues with the database
		}
		
		// Send message
		SimpleMailMessage msg = new SimpleMailMessage();
		msg.setTo(email);
		msg.setFrom(from);
		msg.setSubject(subject);
		msg.setText("Message: " + message +"\nCause: " + cause);
		try {
			javaMailSender.send(msg);
		}catch(Exception e) {
			// Something went wrong
		}
	}
}