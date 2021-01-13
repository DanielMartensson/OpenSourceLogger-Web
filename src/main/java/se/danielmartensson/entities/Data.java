package se.danielmartensson.entities;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
	
	// Label names
	public static final String Analog0 = "A0";
	public static final String Analog1 = "A1";
	public static final String Analog2 = "A2";
	public static final String Analog3 = "A3";
	public static final String SigmaDelta0 = "SA0";
	public static final String SigmaDelta1 = "SA1";
	public static final String SigmaDeltaDifferential1 = "SA1D";
	public static final String SigmaDeltaDifferential2 = "SA2D";
	public static final String SigmaDeltaDifferential3 = "SA3D";
	public static final String PWM0 = "P0";
	public static final String PWM1 = "P1";
	public static final String PWM2 = "P2";
	public static final String PWM3 = "P3";
	public static final String PWM4 = "P4";
	public static final String PWM5 = "P5";
	public static final String PWM6 = "P6";
	public static final String PWM7 = "P7";
	public static final String PWM8 = "P8";
	public static final String DAC0 = "D0";
	public static final String DAC1 = "D1";
	public static final String DAC2 = "D2";
	public static final String JOB_NAME = "Job name";
	public static final String SENSOR_NAME = "Sensor name";
	public static final String CALIBRATION_NAME = "Calibration name";
	public static final String JOB_COMMENT = "Job comment";
	public static final String LOCAL_DATE_TIME = "Local date time";
	public static final String DIGITAL0 = "I0";
	public static final String DIGITAL1 = "I1";
	public static final String DIGITAL2 = "I2";
	public static final String DIGITAL3 = "I3";
	public static final String DIGITAL4 = "I4";
	public static final String DIGITAL5 = "I5";
	public static final String PULSE_NUMBER = "Pulse number";
	public static final String BREAK_PULSE_LIMIT = "Break pulse number";
	public static final String STOP_SIGNAL = "Stop signal";
	public static final String ID = "ID";
	
	// ID
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	// Made by
	private String jobName;
	private String sensorName;
	private String calibrationName;
	private String jobComment;
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
