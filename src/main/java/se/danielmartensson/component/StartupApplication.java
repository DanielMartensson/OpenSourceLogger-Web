package se.danielmartensson.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import se.danielmartensson.entities.MailCheckBox;
import se.danielmartensson.service.MailCheckBoxService;

@Component
public class StartupApplication implements ApplicationListener<ContextRefreshedEvent> {

	@Autowired
	private MailCheckBoxService mailCheckBoxService;

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		fillMailCheckBoxes();
	}

	private void fillMailCheckBoxes() {
		if (mailCheckBoxService.count() == 0) {
			MailCheckBox alarmActiveBox = new MailCheckBox(1, "Alarm Active", false);
			MailCheckBox messageBeenSentBox = new MailCheckBox(2, "Message Has Been Sent", false);
			mailCheckBoxService.save(alarmActiveBox);
			mailCheckBoxService.save(messageBeenSentBox);
		}
	}
}
