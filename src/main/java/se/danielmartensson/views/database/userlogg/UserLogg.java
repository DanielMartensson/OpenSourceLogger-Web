package se.danielmartensson.views.database.userlogg;

import java.time.LocalDate;

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
public class UserLogg {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long loggerId;
	
	@NotNull
	private LocalDate date;
	@NotNull
	private String name;
	@NotNull
	private String comment;
}
