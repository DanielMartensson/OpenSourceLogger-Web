package se.danielmartensson.views.database.datalogg;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DataLoggRepository extends JpaRepository<DataLogg, Long>{
	List<DataLogg> findByLoggerId(long loggerId);
	void deleteByLoggerId(long loggerId);
}
