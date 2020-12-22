package se.danielmartensson.entities;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Alarm {

	// ID
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@NotNull
	private String name;

	// Alarm settings
	@Email
	@NotNull
	private String email;
	@NotNull
	private String message;

	@ManyToMany
	@Fetch(FetchMode.JOIN)
	@NotNull
	private Set<MailCheckBox> checked = new HashSet<>();

	// Thresholds
	@NotNull
	private float sa0Min;
	@NotNull
	private float sa0Max;
	@NotNull
	private float sa1Min;
	@NotNull
	private float sa1Max;
	@NotNull
	private float sa1dMin;
	@NotNull
	private float sa1dMax;
	@NotNull
	private float sa2dMin;
	@NotNull
	private float sa2dMax;
	@NotNull
	private float sa3dMin;
	@NotNull
	private float sa3dMax;
	@NotNull
	private float a0Min;
	@NotNull
	private float a0Max;
	@NotNull
	private float a1Min;
	@NotNull
	private float a1Max;
	@NotNull
	private float a2Min;
	@NotNull
	private float a2Max;
	@NotNull
	private float a3Min;
	@NotNull
	private float a3Max;

	@Override
	public String toString() {
		return name;
	}

}
