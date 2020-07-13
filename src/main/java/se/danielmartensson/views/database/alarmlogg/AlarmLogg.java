package se.danielmartensson.views.database.alarmlogg;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class AlarmLogg {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long AID;
	
	// Comment for these thresholds
	@NotNull
	private String comment;

	// Alarm settings
	private boolean alarm;
	@Email
	private String email;
	
	// Thresholds
	private float AI0Min;
	private float AI0Max;
	private float AI1Min;
	private float AI1Max;
	private float AI2Min;
	private float AI2Max;
	private float AI3Min;
	private float AI3Max;
}
