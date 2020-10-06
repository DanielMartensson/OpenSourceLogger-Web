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
	@NotNull
	private String email;
	
	// Thresholds
	@NotNull
	private float AI0Min;
	@NotNull
	private float AI0Max;
	@NotNull
	private float AI1Min;
	@NotNull
	private float AI1Max;
	@NotNull
	private float AI2Min;
	@NotNull
	private float AI2Max;
	@NotNull
	private float AI3Min;
	@NotNull
	private float AI3Max;
}
