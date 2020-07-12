package se.danielmartensson.views.database.alarmlogg;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AlarmLoggRepository extends JpaRepository<AlarmLogg, Long>{
	AlarmLogg findByAID(long AID);
}
