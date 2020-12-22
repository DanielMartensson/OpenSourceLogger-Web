package se.danielmartensson.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import se.danielmartensson.entities.MailCheckBox;

@Repository
public interface MailCheckBoxRepository extends JpaRepository<MailCheckBox, Long> {

}
