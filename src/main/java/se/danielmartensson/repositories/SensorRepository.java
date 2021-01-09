package se.danielmartensson.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import se.danielmartensson.entities.Calibration;
import se.danielmartensson.entities.Sensor;

@Repository
public interface SensorRepository extends JpaRepository<Sensor, Long> {

	boolean existsByName(String name);
	Sensor findByName(String name);
	boolean existsByCalibration(Calibration calibration);

}
