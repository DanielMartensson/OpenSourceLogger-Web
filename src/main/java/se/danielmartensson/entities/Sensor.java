package se.danielmartensson.entities;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Sensor {

	// ID
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@NotNull
	private String name;
	
	@NotNull
	private String comment;
	
	@ManyToOne
	@NotNull
	private Calibration calibration;

	@NotNull
	private float sa0MinValue;
	@NotNull
	private float sa0MaxValue;
	@NotNull
	private float sa1MinValue;
	@NotNull
	private float sa1MaxValue;
	@NotNull
	private float sa1dMinValue;
	@NotNull
	private float sa1dMaxValue;
	@NotNull
	private float sa2dMinValue;
	@NotNull
	private float sa2dMaxValue;
	@NotNull
	private float sa3dMinValue;
	@NotNull
	private float sa3dMaxValue;
	@NotNull
	private float a0MinValue;
	@NotNull
	private float a0MaxValue;
	@NotNull
	private float a1MinValue;
	@NotNull
	private float a1MaxValue;
	@NotNull
	private float a2MinValue;
	@NotNull
	private float a2MaxValue;
	@NotNull
	private float a3MinValue;
	@NotNull
	private float a3MaxValue;

	@Override
	public String toString() {
		return name;
	}
}
