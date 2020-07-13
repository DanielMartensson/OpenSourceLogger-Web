package se.danielmartensson.views.database.calibrationlogg;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CalibrationLoggRepository extends JpaRepository<CalibrationLogg, Long>{
	CalibrationLogg findByCID(long CID);
}
