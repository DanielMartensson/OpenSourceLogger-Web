package se.danielmartensson.lists;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AnalogOutputs {
	private String variable;
	private String signalVoltage;
	private String resolution;
	private String maxReverseVoltage;
}
