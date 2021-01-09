package se.danielmartensson.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import se.danielmartensson.entities.Alarm;
import se.danielmartensson.entities.Sensor;
import se.danielmartensson.entities.Job;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {
	Job findByName(String name);

	Job findBySensor(Sensor sensor);

	boolean existsBySensor(Sensor sensor);

	boolean existsByAlarm(Alarm alarm);

	boolean existsByName(String name);
}
