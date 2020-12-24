package se.danielmartensson.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import se.danielmartensson.entities.Alarm;

@Repository
public interface AlarmRepository extends JpaRepository<Alarm, Long> {

	boolean existsByName(String name);
	Alarm findByName(String name);

}
