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
	private LocalDateTime dateTime;

	// Analog input
	private float sa0Value;
	private float sa1Value;
	private float sa1dValue;
	private float sa2dValue;
	private float sa3dValue;
	private float a0Value;
	private float a1Value;
	private float a2Value;
	private float a3Value;

	// Digital input
	private boolean i0Value;
	private boolean i1Value;
	private boolean i2Value;
	private boolean i3Value;
	private boolean i4Value;
	private boolean i5Value;

	// Digital PWM
	private int p0Value;
	private int p1Value;
	private int p2Value;
	private int p3Value;
	private int p4Value;
	private int p5Value;
	private int p6Value;
	private int p7Value;
	private int p8Value;

	// Analog output
	private int d0Value;
	private int d1Value;
	private int d2Value;

	// Pulses
	private int pulseNumber;
	private int breakPulseLimit;
	private boolean stopSignal;

}
