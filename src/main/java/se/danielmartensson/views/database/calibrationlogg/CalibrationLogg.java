package se.danielmartensson.views.database.calibrationlogg;

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
	private float SAI0;
	private float BAI0;
	private float SAI1;
	private float BAI1;
	private float SAI2;
	private float BAI2;
	private float SAI3;
	private float BAI3;
	
}
