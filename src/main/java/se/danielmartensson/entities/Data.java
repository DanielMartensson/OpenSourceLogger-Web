package se.danielmartensson.entities;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Data {

	// ID
	@Id
	@GeneratedValue
	private long id;

	// Made by
	private String jobName;
	private String sensorName;
	private String calibrationName;
	@Column(columnDefinition = "DATETIME(3)")
	private LocalDateTime localDateTime;

	// Analog input
	private float sa0;
	private float sa1;
	private float sa1d;
	private float sa2d;
	private float sa3d;
	private float a0;
	private float a1;
	private float a2;
	private float a3;

	// Digital input
	private boolean i0;
	private boolean i1;
	private boolean i2;
	private boolean i3;
	private boolean i4;
	private boolean i5;

	// Digital PWM
	private int p0;
	private int p1;
	private int p2;
	private int p3;
	private int p4;
	private int p5;
	private int p6;
	private int p7;
	private int p8;

	// Analog output
	private int d0;
	private int d1;
	private int d2;

	// Pulses
	private int pulseNumber;
	private int breakPulseLimit;
	private boolean stopSignal;

}
