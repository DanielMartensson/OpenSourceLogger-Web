package se.danielmartensson.entities;

import java.time.LocalDate;

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
public class PWM {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	private String portDescription;
	private int frequencyP0P1P2;
	private int frequencyP3P7P8;
	private int frequencyP6P5;
	private int frequencyP4;
}
