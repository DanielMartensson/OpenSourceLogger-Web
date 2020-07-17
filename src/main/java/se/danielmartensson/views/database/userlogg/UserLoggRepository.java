package se.danielmartensson.views.database.userlogg;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserLoggRepository extends JpaRepository<UserLogg, Long>{
	UserLogg findByLoggerId(long loggerId);
}
