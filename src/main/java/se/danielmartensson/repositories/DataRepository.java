package se.danielmartensson.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import se.danielmartensson.entities.Data;

@Repository
public interface DataRepository extends JpaRepository<Data, Long> {
	void deleteByJobName(String name);

	List<Data> findByJobName(String jobName);

	List<Data> findByJobNameOrderByDateTime(String jobName);
}
