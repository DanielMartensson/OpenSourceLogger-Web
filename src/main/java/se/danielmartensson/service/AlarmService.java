package se.danielmartensson.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import se.danielmartensson.entities.Alarm;
import se.danielmartensson.repositories.AlarmRepository;

@Service
public class AlarmService {

	@Autowired
	private JobService jobService;

	private final AlarmRepository alarmRepository;

	public AlarmService(AlarmRepository alarmRepository) {
		this.alarmRepository = alarmRepository;
	}

	public List<Alarm> findAll() {
		return alarmRepository.findAll();
	}

	public Alarm save(Alarm alarm) {
		return alarmRepository.save(alarm);
	}

	public boolean delete(Alarm alarm) {
		if (jobService.existsByAlarm(alarm)) {
			return true; // Cannot delete child because it's connected to a parent
		} else {
			alarmRepository.delete(alarm); // Delete child because the parent don't exist
			return false;
		}
	}

	public boolean existsByName(String name) {
		return alarmRepository.existsByName(name);
	}
	
	public Alarm findByName(String name) {
		return alarmRepository.findByName(name);
	}
}
