package se.danielmartensson.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class CalibrationLogg {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long CID;
	
	// Comment for these calibration
	@NotNull
	private String comment;
	
	// y = S*AI + B
	@NotNull
	private float SAI0;
	@NotNull
	private float BAI0;
	@NotNull
	private float SAI1;
	@NotNull
	private float BAI1;
	@NotNull
	private float SAI2;
	@NotNull
	private float BAI2;
	@NotNull
	private float SAI3;
	@NotNull
	private float BAI3;
	
}
