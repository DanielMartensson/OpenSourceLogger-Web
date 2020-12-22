package se.danielmartensson.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import se.danielmartensson.entities.Calibration;

@Repository
public interface CalibrationRepository extends JpaRepository<Calibration, Long> {

	boolean existsByName(String name);

}
