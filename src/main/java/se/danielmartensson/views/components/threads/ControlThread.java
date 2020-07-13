package se.danielmartensson.views.components.threads;

import com.pi4j.io.exception.IOException;
import com.pi4j.io.gpio.digital.DigitalInput;
import com.pi4j.io.pwm.Pwm;
import se.danielmartensson.pi4j.ADS1115_ADS1015;
import se.danielmartensson.pi4j.IO;
import se.danielmartensson.views.ControlView;


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
	

	public ControlThread(IO io) {
		pwm0 = io.getPwm0();
		pwm1 = io.getPwm1();
		pwm2 = io.getPwm2();
		pwm3 = io.getPwm3();
		pulseOn = io.getPulseOn();
		stopSignalOn = io.getStopSignalOn();
		ads = io.getAds();
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

			// Control loop - When sampling thread are done, then this will quit too
			while (ControlView.loggingNow.get() == true) {
				// Command signals to the device
				try {
					if(ControlView.do0SliderSelected != do0) {
						pwm0.setDutyCycle(do0/ControlView.MAX_SLIDER_VALE);
						do0 = ControlView.do0SliderSelected;
					}
					if(ControlView.do1SliderSelected != do1) {
						pwm1.setDutyCycle(do1/ControlView.MAX_SLIDER_VALE);
						do1 = ControlView.do1SliderSelected;
					}
					if(ControlView.do2SliderSelected != do2) {
						pwm2.setDutyCycle(do2/ControlView.MAX_SLIDER_VALE);
						do2 = ControlView.do2SliderSelected;
					}
					if(ControlView.do3SliderSelected != do3) {
						pwm3.setDutyCycle(do3/ControlView.MAX_SLIDER_VALE);
						do3 = ControlView.do3SliderSelected;
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

				// Read the input signals
				ai0 = ads.ADSreadADC_SingleEnded(0);
				ai1 = ads.ADSreadADC_SingleEnded(1);
				ai2 = ads.ADSreadADC_SingleEnded(2);
				ai3 = ads.ADSreadADC_SingleEnded(3);

				// Read the pulse
				pulse = pulseOn.isHigh();

				// Read the stop signal
				stopSignal = stopSignalOn.isHigh();

			}
		}
	}
}
