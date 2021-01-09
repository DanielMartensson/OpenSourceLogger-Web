package se.danielmartensson.lists;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AnalogInputs {
	private String variable;
	private String signalAmpere;
	private String signalVoltage;
	private String resolution;
	private String mode;
	private String maxInput;
	private String sensorMinVariable;
	private String sensorMaxVariable;
}
