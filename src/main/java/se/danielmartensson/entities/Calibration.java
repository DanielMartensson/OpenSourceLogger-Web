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
public class Calibration {

	// ID
	@Id
	@GeneratedValue
	private long id;
	@NotNull
	private String name;

	// Measurement = Slope*input + Bias
	@NotNull
	private float sa0Slope;
	@NotNull
	private float sa0Bias;
	@NotNull
	private float sa1Slope;
	@NotNull
	private float sa1Bias;
	@NotNull
	private float sa1dSlope;
	@NotNull
	private float sa1dBias;
	@NotNull
	private float sa2dSlope;
	@NotNull
	private float sa2dBias;
	@NotNull
	private float sa3dSlope;
	@NotNull
	private float sa3dBias;
	@NotNull
	private float a0Slope;
	@NotNull
	private float a0Bias;
	@NotNull
	private float a1Slope;
	@NotNull
	private float a1Bias;
	@NotNull
	private float a2Slope;
	@NotNull
	private float a2Bias;
	@NotNull
	private float a3Slope;
	@NotNull
	private float a3Bias;

	@Override
	public String toString() {
		return name;
	}
}
