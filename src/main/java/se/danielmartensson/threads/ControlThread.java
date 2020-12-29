package se.danielmartensson.threads;

import se.danielmartensson.hardware.Serial;
import se.danielmartensson.views.ControlView;

/**
 * This class read raw and write raw values to the GPIO pins and store them into
 * the static public fields
 * 
 * @author dell
 *
 */
public class ControlThread extends Thread {

	// Signal handler
	private Serial serial;

	// Inputs
	public static int[] ADC;
	public static int[] SDADC;
	public static int[] DSDADC;
	public static boolean[] DI;

	// Outputs
	public static int[] PWM;
	public static int[] DAC;

	public ControlThread(Serial serial) {
		this.serial = serial;

		// Delcare first
		ADC = new int[4]; 		// Analog to digital converter
		SDADC = new int[2]; 	// Sigma delta analog to digital converter
		DSDADC = new int[3]; 	// Differential sigma delta analog to digital converter
		DI = new boolean[6]; 	// Digital input
		PWM = new int[9]; 		// Pulse width modulation
		DAC = new int[3]; 		// Digital to analog converter
	}

	@Override
	public void run() {
		while (true) {
			// Wait loop
			while (!ControlView.loggingNow.get()) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
				}
			}

			// Control loop - When sampling thread are done, then this will quit too
			while (ControlView.loggingNow.get()) {

				// Send PWM and DAC
				PWM[0] = ControlView.sliderSelectedP0;
				PWM[1] = ControlView.sliderSelectedP1;
				PWM[2] = ControlView.sliderSelectedP2;
				PWM[3] = ControlView.sliderSelectedP3;
				PWM[4] = ControlView.sliderSelectedP4;
				PWM[5] = ControlView.sliderSelectedP5;
				PWM[6] = ControlView.sliderSelectedP6;
				PWM[7] = ControlView.sliderSelectedP7;
				PWM[8] = ControlView.sliderSelectedP8;
				DAC[0] = ControlView.sliderSelectedD0;
				DAC[1] = ControlView.sliderSelectedD1;
				DAC[2] = ControlView.sliderSelectedD2;
				serial.transceive(PWM, DAC);

				// Wait
				try {
					Thread.sleep(10); // Some delay does not hurt at all
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				// Receive ADC, SDADC, DSDADC and DI
				serial.receive(ADC, SDADC, DSDADC, DI);
				// Fix -32768 to 0 for SDADC because SDADC is Sigma Delta ADC zero reference, which goes from -2^15 to +2^15, e.g 16-bit total
				for(int i = 0; i < SDADC.length; i++)
					SDADC[i] = SDADC[i] + 32768;
			}
		}
	}
}
