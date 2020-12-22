package se.danielmartensson.service;

import java.util.List;

import org.springframework.stereotype.Service;

import se.danielmartensson.entities.MailCheckBox;
import se.danielmartensson.repositories.MailCheckBoxRepository;

@Service
public class MailCheckBoxService {

	private final MailCheckBoxRepository mailCheckBoxRepository;

	public MailCheckBoxService(MailCheckBoxRepository mailCheckBoxRepository) {
		this.mailCheckBoxRepository = mailCheckBoxRepository;
	}

	public List<MailCheckBox> findAll() {
		return mailCheckBoxRepository.findAll();
	}

	public MailCheckBox save(MailCheckBox mailCheckBox) {
		return mailCheckBoxRepository.save(mailCheckBox);
	}

	public void delete(MailCheckBox mailCheckBox) {
		mailCheckBoxRepository.delete(mailCheckBox);
	}

	public long count() {
		return mailCheckBoxRepository.count();
	}

}
