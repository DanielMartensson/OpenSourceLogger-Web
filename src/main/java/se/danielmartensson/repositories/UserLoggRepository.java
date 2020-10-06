package se.danielmartensson.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import se.danielmartensson.entities.UserLogg;

public interface UserLoggRepository extends JpaRepository<UserLogg, Long>{
	UserLogg findByLoggerId(long loggerId);
}
