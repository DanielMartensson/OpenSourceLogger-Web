package se.danielmartensson.entities;

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
public class Alarm {

	// ID
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
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
	private float sa0MinBreak;
	@NotNull
	private float sa0MaxBreak;
	@NotNull
	private float sa1MinBreak;
	@NotNull
	private float sa1MaxBreak;
	@NotNull
	private float sa1dMinBreak;
	@NotNull
	private float sa1dMaxBreak;
	@NotNull
	private float sa2dMinBreak;
	@NotNull
	private float sa2dMaxBreak;
	@NotNull
	private float sa3dMinBreak;
	@NotNull
	private float sa3dMaxBreak;
	@NotNull
	private float a0MinBreak;
	@NotNull
	private float a0MaxBreak;
	@NotNull
	private float a1MinBreak;
	@NotNull
	private float a1MaxBreak;
	@NotNull
	private float a2MinBreak;
	@NotNull
	private float a2MaxBreak;
	@NotNull
	private float a3MinBreak;
	@NotNull
	private float a3MaxBreak;

	@Override
	public String toString() {
		return name;
	}
	
}
