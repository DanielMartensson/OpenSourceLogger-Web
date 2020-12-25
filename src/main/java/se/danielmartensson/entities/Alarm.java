package se.danielmartensson.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Alarm {

	// ID
	@Id
	@GeneratedValue
	private long id;
	@NotNull
	private String name;

	// Alarm settings
	@Email
	@NotNull
	private String email;
	@NotNull
	private String message;
	
	// Status
	private boolean alarmActive;
	private boolean messageHasBeenSent;

	// Thresholds
	@NotNull
	private float sa0Min;
	@NotNull
	private float sa0Max;
	@NotNull
	private float sa1Min;
	@NotNull
	private float sa1Max;
	@NotNull
	private float sa1dMin;
	@NotNull
	private float sa1dMax;
	@NotNull
	private float sa2dMin;
	@NotNull
	private float sa2dMax;
	@NotNull
	private float sa3dMin;
	@NotNull
	private float sa3dMax;
	@NotNull
	private float a0Min;
	@NotNull
	private float a0Max;
	@NotNull
	private float a1Min;
	@NotNull
	private float a1Max;
	@NotNull
	private float a2Min;
	@NotNull
	private float a2Max;
	@NotNull
	private float a3Min;
	@NotNull
	private float a3Max;

	@Override
	public String toString() {
		return name;
	}

}
