package se.danielmartensson.pi4j;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import com.pi4j.Pi4J;
import com.pi4j.context.Context;
import com.pi4j.exception.Pi4JException;
import com.pi4j.io.gpio.digital.DigitalInput;
import com.pi4j.io.gpio.digital.DigitalInputConfig;
import com.pi4j.io.gpio.digital.DigitalInputProvider;
import com.pi4j.io.gpio.digital.PullResistance;
import com.pi4j.io.pwm.Pwm;
import com.pi4j.io.pwm.PwmConfig;
import com.pi4j.io.pwm.PwmType;
import com.pi4j.plugin.pigpio.provider.pwm.PiGpioPwmProvider;

import lombok.Getter;

@PropertySource("classpath:application.properties")
@Getter
@Component
public class IO {
	
	// Frequency for the PWM 
	@Value("${IO.pi4j.pwmFrequency}")
	private int pwmFrequency;
	
	// PWM outputs
	private Pwm pwm0;
	private Pwm pwm1;
	private Pwm pwm2;
	private Pwm pwm3;

	// Digital inputs
	private DigitalInput pulseOn;
	private DigitalInput stopSignalOn;
	
	// ADS1115 16-Bit ADC
	private ADS1115_ADS1015 ads;
	
	@PostConstruct
	public void init() {
		try {
			Context pi4j = Pi4J.newAutoContext();
			pwm0 = createDigitalPWMOutput(12, pi4j, "pwm0");
			pwm1 = createDigitalPWMOutput(13, pi4j, "pwm1");
			pwm2 = createDigitalPWMOutput(18, pi4j, "pwm2");
			pwm3 = createDigitalPWMOutput(19, pi4j, "pwm3");
			pulseOn = createDigitalInput(23, pi4j, "di0");
			stopSignalOn = createDigitalInput(24, pi4j, "di1");
			ads = new ADS1115_ADS1015(pi4j, 1, ADS1115_ADS1015.ADS_ADDR_GND); // I2C bus = 1
			ads.useADS1115();
			
		} catch (Pi4JException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	// This will read the pulse input and the stop signal
	private DigitalInput createDigitalInput(int pinInput, Context pi4j, String id) {
		try {
			DigitalInputConfig config = DigitalInput.newConfigBuilder(pi4j)
					.id(id)
					.name("Digital input")
					.address(pinInput)
					.pull(PullResistance.PULL_DOWN)
					.debounce(3000L)
					.build();

	        // get a Digital Input I/O provider from the Pi4J context
	        DigitalInputProvider digitalInputProvider = pi4j.provider("pigpio-digital-input");
	        return digitalInputProvider.create(config);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	// This will turn on/off the FQP30N06L MOSFET
	private Pwm createDigitalPWMOutput(int pinOutput, Context pi4j, String id) {
        try {
            // use try-with-resources to auto-close I2C when complete
        	PwmConfig config = Pwm.newConfigBuilder(pi4j)
                    .id(id)
                    .name("PWM Pin output")
                    .address(pinOutput)
                    .pwmType(PwmType.SOFTWARE)
                    .frequency(pwmFrequency)   // optionally pre-configure the desired frequency to 1KHz
                    .dutyCycle(0)     // optionally pre-configure the desired duty-cycle (50%)
                    .shutdown(0)       // optionally pre-configure a shutdown duty-cycle value (on terminate)
                    .initial(0)     // optionally pre-configure an initial duty-cycle value (on startup)
                    .build();
        	Pwm pwm = pi4j.providers().get(PiGpioPwmProvider.class).create(config);
        	pwm.on();
			return pwm;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
