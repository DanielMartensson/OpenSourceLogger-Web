package se.danielmartensson.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import se.danielmartensson.entities.Calibration;
import se.danielmartensson.repositories.CalibrationRepository;

@Service
public class CalibrationService {

	@Autowired
	private JobService jobService;

	private final CalibrationRepository calibrationRepository;

	public CalibrationService(CalibrationRepository calibrationRepository) {
		this.calibrationRepository = calibrationRepository;
	}

	public List<Calibration> findAll() {
		return calibrationRepository.findAll();
	}

	public Calibration save(Calibration calibration) {
		return calibrationRepository.save(calibration);
	}

	public boolean delete(Calibration calibration) {
		if (jobService.existsByCalibration(calibration)) {
			return true; // Cannot delete child because it's connected to a parent
		} else {
			calibrationRepository.delete(calibration); // Delete child because the parent don't exist
			return false;
		}
	}

	public boolean existsByName(String name) {
		return calibrationRepository.existsByName(name);
	}
	
	public Calibration findByName(String name) {
		return calibrationRepository.findByName(name);
	}
}
