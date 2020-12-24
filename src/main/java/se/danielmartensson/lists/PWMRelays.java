package se.danielmartensson.lists;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PWMRelays {
	private String variable;
	private String resolution;
	private String loadConnection;
	private String maxVoltage;
	private String maxLoad;
}
