package se.danielmartensson.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
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
	@NotNull
	private String comment;
	
	@NotNull
	private int sa0MinADC;
	@NotNull
	private int sa0MaxADC;
	@NotNull
	private int sa1MinADC;
	@NotNull
	private int sa1MaxADC;
	@NotNull
	private int sa1dMinADC;
	@NotNull
	private int sa1dMaxADC;
	@NotNull
	private int sa2dMinADC;
	@NotNull
	private int sa2dMaxADC;
	@NotNull
	private int sa3dMinADC;
	@NotNull
	private int sa3dMaxADC;
	@NotNull
	private int a0MinADC;
	@NotNull
	private int a0MaxADC;
	@NotNull
	private int a1MinADC;
	@NotNull
	private int a1MaxADC;
	@NotNull
	private int a2MinADC;
	@NotNull
	private int a2MaxADC;
	@NotNull
	private int a3MinADC;
	@NotNull
	private int a3MaxADC;

	@Override
	public String toString() {
		return name;
	}
}
