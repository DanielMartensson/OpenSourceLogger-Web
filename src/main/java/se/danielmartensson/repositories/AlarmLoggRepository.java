package se.danielmartensson.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import se.danielmartensson.entities.AlarmLogg;

public interface AlarmLoggRepository extends JpaRepository<AlarmLogg, Long>{
	AlarmLogg findByAID(long AID);
}
