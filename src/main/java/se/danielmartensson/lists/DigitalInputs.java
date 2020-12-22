package se.danielmartensson.lists;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class DigitalInputs {
	private String variable;
	private String signalVoltage;
	private String maxInput;
}
