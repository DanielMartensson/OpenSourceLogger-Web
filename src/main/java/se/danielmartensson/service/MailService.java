package se.danielmartensson.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import se.danielmartensson.entities.Alarm;
import se.danielmartensson.entities.MailCheckBox;

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
		// Check if the message has been sent, turn it to true
		boolean messageHasBeenSent = false;
		for (MailCheckBox mailCheckBox : alarm.getChecked()) {
			int boxIndex = (int) mailCheckBox.getId();
			switch (boxIndex) {
			case 2:
				messageHasBeenSent = mailCheckBox.isEnabled(); // This will be false first time, then true
				mailCheckBox.setEnabled(true); // messageHasBeenSent == true
				break;
			}
		}
		if (messageHasBeenSent)
			return;
		
		// Save the mark inside the database
		alarmService.save(alarm);
		
		// Send message
		SimpleMailMessage msg = new SimpleMailMessage();
		msg.setTo(email);
		msg.setSubject(subject);
		msg.setText("Message: " + message +"\nCause: " + cause);
		javaMailSender.send(msg);
	}
}
