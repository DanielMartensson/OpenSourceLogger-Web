package se.danielmartensson.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.danielmartensson.entities.Alarm;
import se.danielmartensson.entities.Sensor;
import se.danielmartensson.entities.Job;
import se.danielmartensson.repositories.JobRepository;

@Service
public class JobService {
	
	@Autowired
	private DataService dataService;

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

	public boolean existById(Long id) {
		return jobRepository.existsById(id);
	}

	public Job findById(Long jobId) {
		return jobRepository.findById(jobId).get();
	}

	public void delete(Job job) {
		dataService.deleteByJobName(job.getName());
		jobRepository.delete(job);
	}

	public Job findBySensor(Sensor sensor) {
		return jobRepository.findBySensor(sensor);
	}

	public boolean existsBySensor(Sensor sensor) {
		return jobRepository.existsBySensor(sensor);
	}

	public boolean existsByAlarm(Alarm alarm) {
		return jobRepository.existsByAlarm(alarm);
	}

	public boolean existsByName(String name) {
		return jobRepository.existsByName(name);
	}
}
