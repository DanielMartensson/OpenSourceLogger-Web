package se.danielmartensson.service;

import java.util.List;

import org.springframework.stereotype.Service;

import se.danielmartensson.entities.PWM;
import se.danielmartensson.repositories.PWMRepository;

@Service
public class PWMService {

	private final PWMRepository pwmRepository;

	public PWMService(PWMRepository pwmRepository) {
		this.pwmRepository = pwmRepository;
	}

	public List<PWM> findAll() {
		return pwmRepository.findAll();
	}

	public PWM save(PWM pwm) {
		return pwmRepository.save(pwm);
	}

	public void delete(PWM pwm) {
		pwmRepository.delete(pwm);
	}

	public long count() {
		return pwmRepository.count();
	}

	public void deleteAll() {
		pwmRepository.deleteAll();
	}
}
