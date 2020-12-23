package se.danielmartensson.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import se.danielmartensson.entities.PWM;

@Repository
public interface PWMRepository extends JpaRepository<PWM, Long> {


}
