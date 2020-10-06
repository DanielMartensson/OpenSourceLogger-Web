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
public class DataLogg {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	// X axis for the plot
	@Column(columnDefinition = "DATETIME(5)") // We want milliseconds too
	private LocalDateTime dateTime;
	
	// Y Axis for the plot
	private int DO0;
	private int DO1;
	private int DO2;
	private int DO3;
	private float AI0;
	private float AI1;
	private float AI2;
	private float AI3;
	
	// Other
	private long loggerId;
	private int samplingTime;
	private int pulseNumber;
	private int breakPulseLimit;
	private boolean stopSignal;
	private String comment;
}
