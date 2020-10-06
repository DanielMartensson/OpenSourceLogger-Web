package se.danielmartensson.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import se.danielmartensson.entities.DataLogg;

public interface DataLoggRepository extends JpaRepository<DataLogg, Long>{
	List<DataLogg> findByLoggerId(long loggerId);
	List<DataLogg> findByLoggerIdOrderByDateTime(long loggerId);
	void deleteByLoggerId(long loggerId);
}
