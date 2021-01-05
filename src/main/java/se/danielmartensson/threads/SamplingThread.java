package se.danielmartensson.threads;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.github.appreciated.apexcharts.ApexCharts;
import com.github.appreciated.apexcharts.helper.Series;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.IntegerField;

import se.danielmartensson.entities.Alarm;
import se.danielmartensson.entities.Calibration;
import se.danielmartensson.entities.Data;
import se.danielmartensson.entities.Job;
import se.danielmartensson.service.DataService;
import se.danielmartensson.service.MailService;
import se.danielmartensson.views.ControlView;
import se.danielmartensson.views.MySQLView;

/**
 * This class plot the values from the ControlTread.java class and also check their thresholds
 * 
 * @author Daniel Mårtensson
 *
 */
public class SamplingThread extends Thread {

	private static final int BIT_15 = 32768;
	private static final int MAX_ATTEMPT = 5;
	
	// Services
	private MailService mailService;
	private DataService dataService;

	// For displaying in the plot
	private Float[] A0;
	private Float[] A1;
	private Float[] A2;
	private Float[] A3;
	private Float[] SA0;
	private Float[] SA1;
	private Float[] SA1D;
	private Float[] SA2D;
	private Float[] SA3D;

	// Counter
	private boolean pastPulse;
	private int pulseNumber;

	// UI components
	private UI ui;
	private ApexCharts apexChart;
	private HorizontalLayout firstRow;
	private Button loggingActivate;
	private List<IntegerField> counters;
	private Checkbox[] inputBoxes;
	private Checkbox[] seriesBoxes;
	
	// Buffert for saving to database later if database connection died
	private List<Data> saveDataLater;

	public SamplingThread(MailService mailService, DataService dataService) {
		this.mailService = mailService;
		this.dataService = dataService;
		saveDataLater = new ArrayList<Data>();
	}

	@Override
	public void run() {
		while (true) {
			// Wait loop
			while (!ControlView.loggingNow.get()) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {}
				
				// Load into database if we have some data that could not be saved directly
				if(saveDataLater.size() > 0) {
					int partIndex = 0;
					if(saveDataLater.size() >= 2000) {
						partIndex = 2000;
					}else {
						partIndex = saveDataLater.size();
					}
					List<Data> insertThese = saveDataLater.subList(0, partIndex);
					try {
						dataService.saveAll(insertThese);
						insertThese.clear();
					}catch(Exception e) {}
				}
			}

			// Series for the plot and restore the plot and counting
			int showSamplesValue = ControlView.selectedShowSamples;
			int samplingTimeValue = ControlView.selectedSamplingTime;
			int selectedBreakPulseLimit = ControlView.selectedBreakPulseLimit;
			A0 = new Float[showSamplesValue];
			A1 = new Float[showSamplesValue];
			A2 = new Float[showSamplesValue];
			A3 = new Float[showSamplesValue];
			SA0 = new Float[showSamplesValue];
			SA1 = new Float[showSamplesValue];
			SA1D = new Float[showSamplesValue];
			SA2D = new Float[showSamplesValue];
			SA3D = new Float[showSamplesValue];

			// Pulses
			pulseNumber = counters.get(0).getValue(); // This is countedPulses object
			pastPulse = false;
			updatePlotAndPulseAndInputs();

			// Get job
			Job job = ControlView.selectedJob;
			String jobName = job.getName();

			// Get calibration
			Calibration calibration = job.getCalibration();
			float sa0Slope = calibration.getSa0Slope();
			float sa0Bias = calibration.getSa0Bias();
			float sa1Slope = calibration.getSa1Slope();
			float sa1Bias = calibration.getSa1Bias();
			float sa1dSlope = calibration.getSa1dSlope();
			float sa1dBias = calibration.getSa1dBias();
			float sa2dSlope = calibration.getSa2dSlope();
			float sa2dBias = calibration.getSa2dBias();
			float sa3dSlope = calibration.getSa3dSlope();
			float sa3dBias = calibration.getSa3dBias();
			float a0Slope = calibration.getA0Slope();
			float a0Bias = calibration.getA0Bias();
			float a1Slope = calibration.getA1Slope();
			float a1Bias = calibration.getA1Bias();
			float a2Slope = calibration.getA2Slope();
			float a2Bias = calibration.getA2Bias();
			float a3Slope = calibration.getA3Slope();
			float a3Bias = calibration.getA3Bias();
			String calibrationName = calibration.getName();

			// Get alarm
			Alarm alarm = job.getAlarm();
			float sa0Min = alarm.getSa0Min();
			float sa0Max = alarm.getSa0Max();
			float sa1Min = alarm.getSa1Min();
			float sa1Max = alarm.getSa1Max();
			float sa1dMin = alarm.getSa1dMin();
			float sa1dMax = alarm.getSa1dMax();
			float sa2dMin = alarm.getSa2dMin();
			float sa2dMax = alarm.getSa2dMax();
			float sa3dMin = alarm.getSa3dMin();
			float sa3dMax = alarm.getSa3dMax();
			float a0Min = alarm.getA0Min();
			float a0Max = alarm.getA0Max();
			float a1Min = alarm.getA1Min();
			float a1Max = alarm.getA1Max();
			float a2Min = alarm.getA2Min();
			float a2Max = alarm.getA2Max();
			float a3Min = alarm.getA3Min();
			float a3Max = alarm.getA3Max();
			String message = alarm.getMessage();
			String email = alarm.getEmail();
			boolean alarmActive = alarm.isAlarmActive();
			
			// Sampling loop
			int connectionAttempts = 0;
			while (ControlView.loggingNow.get()) {

				// Outputs - PWM and DAC
				int p0 = ControlThread.PWM[0];
				int p1 = ControlThread.PWM[1];
				int p2 = ControlThread.PWM[2];
				int p3 = ControlThread.PWM[3];
				int p4 = ControlThread.PWM[4];
				int p5 = ControlThread.PWM[5];
				int p6 = ControlThread.PWM[6];
				int p7 = ControlThread.PWM[7];
				int p8 = ControlThread.PWM[8];
				int d0 = ControlThread.DAC[0];
				int d1 = ControlThread.DAC[1];
				int d2 = ControlThread.DAC[2];

				// Inputs - ADC and SDADC and DSDADC and DI
				float a0 = a0Slope * ControlThread.ADC[3] + a0Bias;
				float a1 = a1Slope * ControlThread.ADC[2] + a1Bias;
				float a2 = a2Slope * ControlThread.ADC[0] + a2Bias;
				float a3 = a3Slope * ControlThread.ADC[1] + a3Bias;
				float sa0 = sa0Slope * (ControlThread.SDADC[0] + BIT_15) + sa0Bias; // This will turn -32768 to 0 if slope is 1 and bias is 0
				float sa1 = sa1Slope * (ControlThread.SDADC[1] + BIT_15) + sa1Bias;
				float sa1d = sa1dSlope * ControlThread.DSDADC[0] + sa1dBias;
				float sa2d = sa2dSlope * ControlThread.DSDADC[1] + sa2dBias;
				float sa3d = sa3dSlope * ControlThread.DSDADC[2] + sa3dBias;
				boolean di0 = ControlThread.DI[0]; // Counter
				boolean di1 = ControlThread.DI[1]; // Stop signal
				boolean di2 = ControlThread.DI[2];
				boolean di3 = ControlThread.DI[3];
				boolean di4 = ControlThread.DI[4];
				boolean di5 = ControlThread.DI[5];

				// Read the pulse signal and count it if...
				if (di0 != pastPulse && di0 == true)
					pulseNumber++;
				pastPulse = di0;

				// Read the alarm signal if the alarm is active
				boolean stopSignal = false;
				if(alarmActive)
					stopSignal = di1;

				// Save them to the database
				Data dataLogg = new Data(0, jobName, calibrationName, LocalDateTime.now(), sa0, sa1, sa1d, sa2d, sa3d, a0, a1, a2, a3, di0, di1, di2, di3, di4, di5, p0, p1, p2, p3, p4, p5, p6, p7, p8, d0, d1, d2, pulseNumber, selectedBreakPulseLimit, stopSignal);
				try {
					dataService.save(dataLogg);
					connectionAttempts = 0; 
				}catch(Exception e) {
					if(connectionAttempts > MAX_ATTEMPT) {
						mailService.sendMessage(alarm, email, "Max database connection attempts(" + MAX_ATTEMPT + ") achieved. Logging shutting down.\n" + e.getMessage(), message);
						break; // This will jump out of the while loop
					}else {
						connectionAttempts++;
					}
					saveDataLater.add(dataLogg); // Save it for later if something just happen
				}

				// Show the values on the plot - First shift it back 1 step, set the last
				// element and update the plot
				if (ControlView.selectedShowPlot) {
					Collections.rotate(Arrays.asList(A0), -1);
					Collections.rotate(Arrays.asList(A1), -1);
					Collections.rotate(Arrays.asList(A2), -1);
					Collections.rotate(Arrays.asList(A3), -1);
					Collections.rotate(Arrays.asList(SA0), -1);
					Collections.rotate(Arrays.asList(SA1), -1);
					Collections.rotate(Arrays.asList(SA1D), -1);
					Collections.rotate(Arrays.asList(SA2D), -1);
					Collections.rotate(Arrays.asList(SA3D), -1);
					A0[showSamplesValue - 1] = a0;
					A1[showSamplesValue - 1] = a1;
					A2[showSamplesValue - 1] = a2;
					A3[showSamplesValue - 1] = a3;
					SA0[showSamplesValue - 1] = sa0;
					SA1[showSamplesValue - 1] = sa1;
					SA1D[showSamplesValue - 1] = sa1d;
					SA2D[showSamplesValue - 1] = sa2d;
					SA3D[showSamplesValue - 1] = sa3d;
					updatePlotAndPulseAndInputs();
				}

				// If we exceeded the thresholds
				boolean breakThreshHold0 = (a0 > a0Max) || (a1 < a0Min);
				boolean breakThreshHold1 = (a1 > a1Max) || (a1 < a1Min);
				boolean breakThreshHold2 = (a2 > a2Max) || (a2 < a2Min);
				boolean breakThreshHold3 = (a3 > a3Max) || (a3 < a3Min);
				boolean breakThreshHold4 = (sa0 > sa0Max) || (sa0 < sa0Min);
				boolean breakThreshHold5 = (sa1 > sa1Max) || (sa1 < sa1Min);
				boolean breakThreshHold6 = (sa1d > sa1dMax) || (sa1d < sa1dMin);
				boolean breakThreshHold7 = (sa2d > sa2dMax) || (sa2d < sa2dMin);
				boolean breakThreshHold8 = (sa3d > sa3dMax) || (sa3d < sa3dMin);
				
				if(alarmActive) {
					if (breakThreshHold0) {
						mailService.sendMessage(alarm, email, "Mesurement exceeded the threshold at analog input 0.", message);
						break;
					} else if (breakThreshHold1) {
						mailService.sendMessage(alarm, email, "Mesurement exceeded the threshold at analog input 1.", message);
						break;
					} else if (breakThreshHold2) {
						mailService.sendMessage(alarm, email, "Mesurement exceeded the threshold at analog input 2.", message);
						break;
					} else if (breakThreshHold3) {
						mailService.sendMessage(alarm, email, "Mesurement exceeded the threshold at analog input 3.", message);
						break;
					} else if (breakThreshHold4) {
						mailService.sendMessage(alarm, email, "Mesurement exceeded the threshold at sigma delta analog input 0.", message);
						break;
					} else if (breakThreshHold5) {
						mailService.sendMessage(alarm, email, "Mesurement exceeded the threshold at sigma delta analog input 1.", message);
						break;
					} else if (breakThreshHold6) {
						mailService.sendMessage(alarm, email, "Mesurement exceeded the threshold at differential sigma delta analog input 1.", message);
						break;
					} else if (breakThreshHold7) {
						mailService.sendMessage(alarm, email, "Mesurement exceeded the threshold at differential sigma delta analog input 2.", message);
						break;
					} else if (breakThreshHold8) {
						mailService.sendMessage(alarm, email, "Mesurement exceeded the threshold at differential sigma delta analog input 3.", message);
						break;
					} else if (stopSignal) {
						mailService.sendMessage(alarm, email, "Stop signal activated.", message);
						break;
					} else if (pulseNumber >= selectedBreakPulseLimit) {
						mailService.sendMessage(alarm, email, "Number of pulse limits exceeded", message);
						break;
					}
				}

				// Wait or we could break the while loop by pushing the stop button
				int sampleTimeCounter = 0;
				while(sampleTimeCounter < samplingTimeValue && ControlView.loggingNow.get()) {
					try {
						Thread.sleep(1);
					} catch (InterruptedException e) {}
					sampleTimeCounter++;
				}
			}

			// This will cause so components will be enabled again - and also the control thread stops!
			ControlView.loggingNow.set(false); // OFF
			updatePlotAndPulseAndInputs();
		}
	}

	private void updatePlotAndPulseAndInputs() {
		if(!ui.isClosing()) {
			ui.access(() -> {
				
				// Count how many series boxes that are being checked
				int boxesChecked = 0;
				for(Checkbox seriesBox : seriesBoxes) {
					if(seriesBox.getValue())
						boxesChecked++;
				}
				
				// Create the series list
				Series<?>[] seriesList = new Series[boxesChecked];
				int seriesIndex = 0;
				int seriesBoxesIndex = 0;
				for(Checkbox seriesBox : seriesBoxes) {
					seriesBoxesIndex++; // Will be starting at 1
					if(!seriesBox.getValue())
						continue;
						
					switch(seriesBoxesIndex) {
						case 1:
							seriesList[seriesIndex] = MySQLView.createSerie(A0, "A0");
							break;
						case 2:
							seriesList[seriesIndex] = MySQLView.createSerie(A1, "A1");
							break;
						case 3:
							seriesList[seriesIndex] = MySQLView.createSerie(A2, "A2");
							break;
						case 4:
							seriesList[seriesIndex] = MySQLView.createSerie(A3, "A3");
							break;
						case 5:
							seriesList[seriesIndex] = MySQLView.createSerie(SA0, "SA0");
							break;
						case 6:
							seriesList[seriesIndex] = MySQLView.createSerie(SA1, "SA1");
							break;
						case 7:
							seriesList[seriesIndex] = MySQLView.createSerie(SA1D, "SA1D");
							break;
						case 8:
							seriesList[seriesIndex] = MySQLView.createSerie(SA2D, "SA2D");
							break;
						case 9:
							seriesList[seriesIndex] = MySQLView.createSerie(SA3D, "SA3D");
							break;
					}
					seriesIndex++;
				}
		
				// Update the plot now
				if(boxesChecked > 0)
					apexChart.updateSeries(seriesList);
				
				// This is countedPulses
				counters.get(0).setValue(pulseNumber); 
				
				// This is the digital inputs
				for (int i = 0; i < inputBoxes.length; i++)
					inputBoxes[i].setValue(ControlThread.DI[i]);
				disableOrEnableComponents();
			});
		}

	}

	private void disableOrEnableComponents() {
		if (ControlView.loggingNow.get()) {
			// This runs when we don't run the logging
			loggingActivate.setText(ControlView.STOP_LOGGING);
			firstRow.setEnabled(false);
		} else {
			loggingActivate.setText(ControlView.START_LOGGING);
			firstRow.setEnabled(true);
		}
	}

	public void setComponentsToThread(UI ui, HorizontalLayout firstRow, Button loggingActivate, ApexCharts apexChart, List<IntegerField> counters, Checkbox[] inputBoxes, Checkbox[] seriesBoxes) {
		this.ui = ui;
		this.firstRow = firstRow;
		this.apexChart = apexChart;
		this.loggingActivate = loggingActivate;
		this.counters = counters;
		this.inputBoxes = inputBoxes;
		this.seriesBoxes = seriesBoxes;
		disableOrEnableComponents(); // Once we have set our components, disable them or not.
	}
}