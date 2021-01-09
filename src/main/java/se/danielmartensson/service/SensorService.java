package se.danielmartensson.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import se.danielmartensson.entities.Calibration;
import se.danielmartensson.entities.Sensor;
import se.danielmartensson.repositories.SensorRepository;

@Service
public class SensorService {

	@Autowired
	private JobService jobService;

	private final SensorRepository sensorRepository;

	public SensorService(SensorRepository sensorRepository) {
		this.sensorRepository = sensorRepository;
	}

	public List<Sensor> findAll() {
		return sensorRepository.findAll();
	}

	public Sensor save(Sensor sensor) {
		return sensorRepository.save(sensor);
	}

	public boolean delete(Sensor sensor) {
		if (jobService.existsBySensor(sensor)) {
			return false; // Cannot delete child because it's connected to a parent
		} else {
			sensorRepository.delete(sensor); // Delete child because the parent don't exist
			return true;
		}
	}

	public boolean existsByName(String name) {
		return sensorRepository.existsByName(name);
	}
	
	public Sensor findByName(String name) {
		return sensorRepository.findByName(name);
	}

	public boolean existsByCalibration(Calibration calibration) {
		return sensorRepository.existsByCalibration(calibration);
	}
}
