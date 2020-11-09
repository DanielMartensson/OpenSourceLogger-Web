package se.danielmartensson.threads;

import com.pi4j.io.exception.IOException;
import com.pi4j.io.gpio.digital.DigitalInput;
import com.pi4j.io.pwm.Pwm;
import se.danielmartensson.pi4j.ADS1115_ADS1015;
import se.danielmartensson.pi4j.IO;
import se.danielmartensson.views.ControlView;


/**
 * This class read raw and write raw values to the GPIO pins and store them into the static public fields
 * @author dell
 *
 */
public class ControlThread extends Thread {

	// Digital signals out 
	static public int do0;
	static public int do1;
	static public int do2;
	static public int do3;

	// Analog signals in 
	static public int ai0;
	static public int ai1;
	static public int ai2;
	static public int ai3;

	// Boolean signal 
	static public boolean pulse;
	static public boolean stopSignal;
	
	// IO
	private Pwm pwm0;
	private Pwm pwm1;
	private Pwm pwm2;
	private Pwm pwm3;
	private DigitalInput pulseOn;
	private DigitalInput stopSignalOn;
	private ADS1115_ADS1015 ads;
	private int adcAt4mAforAnalog0;
	private int adcAt4mAforAnalog1;
	private int adcAt4mAforAnalog2;
	private int adcAt4mAforAnalog3;
	

	public ControlThread(IO io, int adcAt4mAforAnalog0, int adcAt4mAforAnalog1, int adcAt4mAforAnalog2, int adcAt4mAforAnalog3) {
		pwm0 = io.getPwm0();
		pwm1 = io.getPwm1();
		pwm2 = io.getPwm2();
		pwm3 = io.getPwm3();
		pulseOn = io.getPulseOn();
		stopSignalOn = io.getStopSignalOn();
		ads = io.getAds();
		this.adcAt4mAforAnalog0 = adcAt4mAforAnalog0;
		this.adcAt4mAforAnalog1 = adcAt4mAforAnalog1;
		this.adcAt4mAforAnalog2 = adcAt4mAforAnalog2;
		this.adcAt4mAforAnalog3 = adcAt4mAforAnalog3;
	}

	@Override
	public void run() {
		while (true) {
			// Wait loop
			while (ControlView.loggingNow.get() == false) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
				}
			}
			
			// Pulses counter
			boolean do0LowFirst = ControlView.do0LowFirst;
			boolean do1LowFirst = ControlView.do1LowFirst;
			boolean do2LowFirst = ControlView.do2LowFirst;
			boolean do3LowFirst = ControlView.do3LowFirst;
			long counterForPulsesD0 = 0;
			long counterForPulsesD1 = 0;
			long counterForPulsesD2 = 0;
			long counterForPulsesD3 = 0;
			
			// What program should we use
			String selectedProgram = ControlView.selectedProgram;
			
			int[] sliderSlected = new int[4];
			int[] highPulseSelected = new int[4];
			int[] lowPulseSelected = new int[4];
			long[] counterForPulses = new long[4];
			boolean[] lowFirst = {do0LowFirst, do1LowFirst, do2LowFirst, do3LowFirst};
			
			// Control loop - When sampling thread are done, then this will quit too
			while (ControlView.loggingNow.get() == true) {
				// Change the sliderSelected by using this loop program
				if(selectedProgram.equals(ControlView.PULSE_PROGRAM)) {
					
					// Set
					sliderSlected[0] = ControlView.do0SliderSelected;
					sliderSlected[1] = ControlView.do1SliderSelected;
					sliderSlected[2] = ControlView.do2SliderSelected;
					sliderSlected[3] = ControlView.do3SliderSelected;
					highPulseSelected[0] = ControlView.do0HighPulseSelected;
					highPulseSelected[1] = ControlView.do1HighPulseSelected;
					highPulseSelected[2] = ControlView.do2HighPulseSelected;
					highPulseSelected[3] = ControlView.do3HighPulseSelected;
					lowPulseSelected[0] = ControlView.do0HighPulseSelected;
					lowPulseSelected[1] = ControlView.do1LowPulseSelected;
					lowPulseSelected[2] = ControlView.do2LowPulseSelected;
					lowPulseSelected[3] = ControlView.do3LowPulseSelected;
					counterForPulses[0] = counterForPulsesD0;
					counterForPulses[1] = counterForPulsesD1;
					counterForPulses[2] = counterForPulsesD2;
					counterForPulses[3] = counterForPulsesD3;
					
					// Do things
					changeSliderValues(sliderSlected, highPulseSelected, lowPulseSelected, counterForPulses, lowFirst);
					
					// Get
					ControlView.do0SliderSelected = sliderSlected[0];
					ControlView.do1SliderSelected = sliderSlected[1];
					ControlView.do2SliderSelected = sliderSlected[2];
					ControlView.do3SliderSelected = sliderSlected[3];
					ControlView.do0HighPulseSelected = highPulseSelected[0];
					ControlView.do1HighPulseSelected = highPulseSelected[1];
					ControlView.do2HighPulseSelected = highPulseSelected[2];
					ControlView.do3HighPulseSelected = highPulseSelected[3];
					ControlView.do0HighPulseSelected = lowPulseSelected[0];
					ControlView.do1LowPulseSelected = lowPulseSelected[1];
					ControlView.do2LowPulseSelected = lowPulseSelected[2];
					ControlView.do3LowPulseSelected = lowPulseSelected[3];
					counterForPulsesD0 = counterForPulses[0];
					counterForPulsesD1 = counterForPulses[1];
					counterForPulsesD2 = counterForPulses[2];
					counterForPulsesD3 = counterForPulses[3];
					
				}
				
				// Command signals to the device with change only
				try {
					if(ControlView.do0SliderSelected != do0) {
						do0 = ControlView.do0SliderSelected;
						pwm0.setDutyCycle(do0/ControlView.MAX_SLIDER_VALE);
					}
					if(ControlView.do1SliderSelected != do1) {
						do1 = ControlView.do1SliderSelected;
						pwm1.setDutyCycle(do1/ControlView.MAX_SLIDER_VALE);
					}
					if(ControlView.do2SliderSelected != do2) {
						do2 = ControlView.do2SliderSelected;
						pwm2.setDutyCycle(do2/ControlView.MAX_SLIDER_VALE);
					}
					if(ControlView.do3SliderSelected != do3) {
						do3 = ControlView.do3SliderSelected;
						pwm3.setDutyCycle(do3/ControlView.MAX_SLIDER_VALE);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}

				// Wait
				try {
					Thread.sleep(1); // Fast as possible
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				// Read the input 4-20 mA signals
				int rawAi0 = ads.ADSreadADC_SingleEnded(0);
				if(rawAi0 < adcAt4mAforAnalog0)
					ai0 = 0; // No sensor connected
				else
					ai0 = rawAi0 - adcAt4mAforAnalog0; // At 4mA, then ai0 = 0
				
				int rawAi1 = ads.ADSreadADC_SingleEnded(1);
				if(rawAi1 < adcAt4mAforAnalog1)
					ai1 = 0; 
				else
					ai1 = rawAi1 - adcAt4mAforAnalog1;
				
				int rawAi2 = ads.ADSreadADC_SingleEnded(2);
				if(rawAi2 < adcAt4mAforAnalog2)
					ai2 = 0; 
				else
					ai2 = rawAi2 - adcAt4mAforAnalog2;
				
				int rawAi3 = ads.ADSreadADC_SingleEnded(3);
				if(rawAi3 < adcAt4mAforAnalog3)
					ai3 = 0; 
				else
					ai3 = rawAi3 - adcAt4mAforAnalog3; 
				
	
				// Read the pulse
				pulse = pulseOn.isHigh();

				// Read the stop signal
				stopSignal = stopSignalOn.isHigh();

			}
		}
	}

	private void changeSliderValues(int[] sliderSlected, int[] highPulseSelected, int[] lowPulseSelected, long[] counterForPulses, boolean[] lowFirst) {
		for(int i = 0; i < sliderSlected.length; i++) {
			// <Zero>---4095 PWM----<HighPulseSelected>----0 PWM-----<LowPulseSelected>
			// <Zero>-------------------------------counterForPulses------------------------------<HighPulseSelected + LowPulseSelected>
			
			if((counterForPulses[i] <= highPulseSelected[i]) && (counterForPulses[i] <= lowPulseSelected[i]))
				sliderSlected[i] = ControlView.MAX_SLIDER_VALE;
			else
				sliderSlected[i] = 0;
		
			// Count the period
			if(lowFirst[i]) {
				if((counterForPulses[i] > 0) && (counterForPulses[i] <= highPulseSelected[i] + lowPulseSelected[i]))
					counterForPulses[i]--;
				else 
					counterForPulses[i] = highPulseSelected[i] + lowPulseSelected[i];
			}else {
				if(counterForPulses[i] < highPulseSelected[i] + lowPulseSelected[i])
					counterForPulses[i]++;
				else
					counterForPulses[i] = 0;
			}
		}
	}
}
