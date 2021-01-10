package se.danielmartensson.entities;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Job {
	@Id
	@GeneratedValue
	private long id;

	@NotNull
	private LocalDate date;
	@NotNull
	private String name;
	@ManyToOne
	@NotNull
	private Alarm alarm;
	@ManyToOne
	@NotNull
	private Sensor sensor;

	@Override
	public String toString() {
		return name;
	}
}