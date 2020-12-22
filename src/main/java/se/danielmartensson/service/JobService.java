package se.danielmartensson.service;

import java.util.List;
import org.springframework.stereotype.Service;

import se.danielmartensson.entities.Alarm;
import se.danielmartensson.entities.Calibration;
import se.danielmartensson.entities.Job;
import se.danielmartensson.repositories.JobRepository;

@Service
public class JobService {

	private final JobRepository jobRepository;

	public JobService(JobRepository jobRepository) {
		this.jobRepository = jobRepository;
	}

	public List<Job> findAll() {
		return jobRepository.findAll();
	}

	public Job save(Job job) {
		return jobRepository.save(job);
	}

	public Job findByName(String name) {
		return jobRepository.findByName(name);
	}

	public boolean existById(long id) {
		return jobRepository.existsById(id);
	}

	public Job findById(long id) {
		return jobRepository.findById(id).get();
	}

	public void delete(Job job) {
		jobRepository.delete(job);
	}

	public Job findByCalibration(Calibration calibration) {
		return jobRepository.findByCalibration(calibration);
	}

	public boolean existsByCalibration(Calibration calibration) {
		return jobRepository.existsByCalibration(calibration);
	}

	public boolean existsByAlarm(Alarm alarm) {
		return jobRepository.existsByAlarm(alarm);
	}

	public boolean existsByName(String name) {
		return jobRepository.existsByName(name);
	}

}
