package se.danielmartensson.entities;

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
public class MailCheckBox {
	@Id
	@GeneratedValue
	private long id;

	private String name;
	private boolean enabled;

	@Override
	public String toString() {
		return name;
	}
}
