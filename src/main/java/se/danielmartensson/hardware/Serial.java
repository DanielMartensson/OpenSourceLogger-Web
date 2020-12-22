package se.danielmartensson.hardware;

import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import com.fazecast.jSerialComm.SerialPort;

import lombok.Getter;
import lombok.Setter;
import se.danielmartensson.entities.PWM;
import se.danielmartensson.service.PWMService;

@Component
@PropertySource("classpath:application.properties")
@Setter
public class Serial {

	private static final byte WRITE_PWM_DAC_PERIODS = 49;
	private static final byte WRITE_PWM_FREQUENCIES = 50;
	private static final int MESSAGE_LENGTH = 25;
	private SerialPort selectedSerialPort;
	private SerialPort[] serialPorts;
	
	@Autowired
	private PWMService pwmService;
	
	@PostConstruct
	public void init() {
		serialPorts = getSerialPorts();
		List<PWM> pwmList = pwmService.findAll();
		if(pwmList.size() > 0)
			selectNewPort(pwmList.get(0).getPortDescription()); // We ONLY have one row in this PWM database
	}
	
	public SerialPort[] getSerialPorts() {
		return SerialPort.getCommPorts();
	}
	
	private void openPort() {
		selectedSerialPort.openPort();
	}
	
	public void closePort() {
		if(selectedSerialPort != null)
			selectedSerialPort.closePort();
	}
	
	public boolean isPortOpen() {
		if(selectedSerialPort != null)
			return selectedSerialPort.isOpen();
		return false;
	}
	
	public void selectNewPort(String portDescription) {
		for (SerialPort serialPort : serialPorts) {
			if (serialPort.getPortDescription().contains(portDescription)) {
				serialPort.setComPortParameters(115200, 8, 1, SerialPort.NO_PARITY);
				selectedSerialPort = serialPort;
				openPort();
				break;
			}
		}
	}
	
	public void trancieve(int frequency[]) {
		if (selectedSerialPort == null)
			return;
		byte[] buffer = new byte[MESSAGE_LENGTH]; // Settings for the PWM timers
		buffer[0] = WRITE_PWM_FREQUENCIES;
		fillBuffer(buffer, frequency, 1);
		selectedSerialPort.writeBytes(buffer, buffer.length);
	}

	public void transceive(int[] PWM, int[] DAC) {
		if (selectedSerialPort == null)
			return;
		byte[] buffer = new byte[MESSAGE_LENGTH]; // DAC and PWM have range 0-4095, eg 12-bit, but they have room for 16-bit
		buffer[0] = WRITE_PWM_DAC_PERIODS; 
		fillBuffer(buffer, PWM, 1);
		fillBuffer(buffer, DAC, PWM.length*2 + 1);
		selectedSerialPort.writeBytes(buffer, buffer.length); // Read with uint16_t = (buffer[i + i] << 8) | (buffer[i + i + 1] & 0xFF);
	}

	private void fillBuffer(byte[] byteArray, int[] intArray, int elementsThatHasBeenWritten) {
		for (int i = 0; i < intArray.length; i++) {
			byteArray[i + i + elementsThatHasBeenWritten] = (byte) (intArray[i] >> 8);
			byteArray[i + i + 1 + elementsThatHasBeenWritten] = (byte) intArray[i];
		}
	}

	public void receive(int[] ADC, int[] SDADC, int[] DSDADC, boolean[] DI) {
		if (selectedSerialPort == null)
			return;
		int size = selectedSerialPort.bytesAvailable();
		if (size < ADC.length * 2 + SDADC.length * 2 + DSDADC.length * 2 + DI.length)
			return;

		byte[] buffer = new byte[size]; // We will always get a fixed size
		selectedSerialPort.readBytes(buffer, buffer.length);

		// Read ADC, SDADC, DSDADC and DI
		fillArray(buffer, ADC, 0);
		fillArray(buffer, SDADC, ADC.length * 2);
		fillArray(buffer, DSDADC, (ADC.length + SDADC.length) * 2);
		fillArray(buffer, DI, (ADC.length + SDADC.length + DSDADC.length) * 2);
	}


	private void fillArray(byte[] buffer, boolean[] booleanArray, int elementsThatHasBeenWritten) {
		for (int i = 0; i < booleanArray.length; i++) {
			booleanArray[i] = buffer[i + elementsThatHasBeenWritten] == 1 ? true : false;
		}
	}

	private void fillArray(byte[] byteArray, int[] intArray, int elementsThatHasBeenWritten) {
		for (int i = 0; i < intArray.length; i++) {
			intArray[i] = (byteArray[i + i + elementsThatHasBeenWritten] << 8) | (byteArray[i + i + 1 + elementsThatHasBeenWritten] & 0xFF);
		}
	}
}