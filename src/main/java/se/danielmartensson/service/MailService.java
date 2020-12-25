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
    
    @Autowired
    private AlarmService alarmService;
    
   
	public MailService() {
		
	}

	public void sendMessage(Alarm alarm, String email, String cause, String message) {
		if (alarm.isMessageHasBeenSent())
			return;
		
		// Save the mark inside the database
		alarm.setMessageHasBeenSent(true);
		alarmService.save(alarm);
		
		// Send message
		SimpleMailMessage msg = new SimpleMailMessage();
		msg.setTo(email);
		msg.setSubject(subject);
		msg.setText("Message: " + message +"\nCause: " + cause);
		try {
			javaMailSender.send(msg);
		}catch(Exception e) {
			// Something went wrong
		}
	}
}