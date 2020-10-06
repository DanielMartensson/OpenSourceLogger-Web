package se.danielmartensson.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import se.danielmartensson.entities.CalibrationLogg;

public interface CalibrationLoggRepository extends JpaRepository<CalibrationLogg, Long>{
	CalibrationLogg findByCID(long CID);
}
