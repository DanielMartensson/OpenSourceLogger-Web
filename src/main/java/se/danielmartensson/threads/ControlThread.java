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
				
				// This turn down PWM and DAC if we are not logging - Safety feature
				talkToSTM32(0,0,0,0,0,0,0,0,0,0,0,0);
			}

			// Control loop - When sampling thread are done, then this will quit too
			while (ControlView.loggingNow.get()) {
				// Send PWM and DAC to STM32 and receive measurements
				talkToSTM32(ControlView.sliderSelectedP0, 
							ControlView.sliderSelectedP1,
							ControlView.sliderSelectedP2,
							ControlView.sliderSelectedP3,
							ControlView.sliderSelectedP4,
							ControlView.sliderSelectedP5,
							ControlView.sliderSelectedP6,
							ControlView.sliderSelectedP7,
							ControlView.sliderSelectedP8,
							ControlView.sliderSelectedD0,
							ControlView.sliderSelectedD1,
							ControlView.sliderSelectedD2);
			}
		}
	}

	private void talkToSTM32(int P0, int P1, int P2, int P3, int P4, int P5, int P6, int P7, int P8, int D0, int D1, int D2) {
		// Ask if ready and then check
		serial.askIfReady();
		if(!serial.isOK())
			return;

		// Send PWM and DAC
		PWM[0] = P0;
		PWM[1] = P1;
		PWM[2] = P2;
		PWM[3] = P3;
		PWM[4] = P4;
		PWM[5] = P5;
		PWM[6] = P6;
		PWM[7] = P7;
		PWM[8] = P8;
		DAC[0] = D0;
		DAC[1] = D1;
		DAC[2] = D2;
		serial.transceive_PWM_DAC(PWM, DAC);

		// Receive ADC, SDADC, DSDADC and DI
		serial.receive_ADC_SDADC_DSDADC_DI(ADC, SDADC, DSDADC, DI);
	}
}
