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
import se.danielmartensson.entities.Sensor;
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

			// Get alarm
			Alarm alarm = job.getAlarm();
			float sa0MinBreak = alarm.getSa0MinBreak();
			float sa0MaxBreak = alarm.getSa0MaxBreak();
			float sa1MinBreak = alarm.getSa1MinBreak();
			float sa1MaxBreak = alarm.getSa1MaxBreak();
			float sa1dMinBreak = alarm.getSa1dMinBreak();
			float sa1dMaxBreak = alarm.getSa1dMaxBreak();
			float sa2dMinBreak = alarm.getSa2dMinBreak();
			float sa2dMaxBreak = alarm.getSa2dMaxBreak();
			float sa3dMinBreak = alarm.getSa3dMinBreak();
			float sa3dMaxBreak = alarm.getSa3dMaxBreak();
			float a0MinBreak = alarm.getA0MinBreak();
			float a0MaxBreak = alarm.getA0MaxBreak();
			float a1MinBreak = alarm.getA1MinBreak();
			float a1MaxBreak = alarm.getA1MaxBreak();
			float a2MinBreak = alarm.getA2MinBreak();
			float a2MaxBreak = alarm.getA2MaxBreak();
			float a3MinBreak = alarm.getA3MinBreak();
			float a3MaxBreak = alarm.getA3MaxBreak();
			String message = alarm.getMessage();
			String email = alarm.getEmail();
			boolean alarmActive = alarm.isAlarmActive();
			
			// Get sensor
			Sensor sensor = job.getSensor();
			float sa0MinValue = sensor.getSa0MinValue();
			float sa0MaxValue = sensor.getSa0MaxValue();
			float sa1MinValue = sensor.getSa1MinValue();
			float sa1MaxValue = sensor.getSa1MaxValue();
			float sa1dMinValue = sensor.getSa1dMinValue();
			float sa1dMaxValue = sensor.getSa1dMaxValue();
			float sa2dMinValue = sensor.getSa2dMinValue();
			float sa2dMaxValue = sensor.getSa2dMaxValue();
			float sa3dMinValue = sensor.getSa3dMinValue();
			float sa3dMaxValue = sensor.getSa3dMaxValue();
			float a0MinValue = sensor.getA0MinValue();
			float a0MaxValue = sensor.getA0MaxValue();
			float a1MinValue = sensor.getA1MinValue();
			float a1MaxValue = sensor.getA1MaxValue();
			float a2MinValue = sensor.getA2MinValue();
			float a2MaxValue = sensor.getA2MaxValue();
			float a3MinValue = sensor.getA3MinValue();
			float a3MaxValue = sensor.getA3MaxValue();
			String sensorName = sensor.getName();
			
			// Get calibration
			Calibration calibration = sensor.getCalibration();
			int sa0MinADC = calibration.getSa0MinADC();
			int sa0MaxADC = calibration.getSa0MaxADC() != sa0MinADC ? calibration.getSa0MaxADC() : sa0MinADC + 1; // This prevent us to divide by zero
			int sa1MinADC = calibration.getSa1MinADC();
			int sa1MaxADC = calibration.getSa1MaxADC() != sa1MinADC ? calibration.getSa1MaxADC() : sa1MinADC + 1;
			int sa1dMinADC = calibration.getSa1dMinADC();
			int sa1dMaxADC = calibration.getSa1dMaxADC() != sa1dMinADC ? calibration.getSa1dMaxADC() : sa1dMinADC + 1;
			int sa2dMinADC = calibration.getSa2dMinADC();
			int sa2dMaxADC = calibration.getSa2dMaxADC() != sa2dMinADC ? calibration.getSa2dMaxADC() : sa2dMinADC + 1;
			int sa3dMinADC = calibration.getSa3dMinADC();
			int sa3dMaxADC = calibration.getSa3dMaxADC() != sa3dMinADC ? calibration.getSa3dMaxADC() : sa3dMinADC + 1;
			int a0MinADC = calibration.getA0MinADC();
			int a0MaxADC = calibration.getA0MaxADC() != a0MinADC ? calibration.getA0MaxADC() : a0MinADC + 1;
			int a1MinADC = calibration.getA1MinADC();
			int a1MaxADC = calibration.getA1MaxADC() != a1MinADC ? calibration.getA1MaxADC() : a1MinADC + 1;
			int a2MinADC = calibration.getA2MinADC();
			int a2MaxADC = calibration.getA2MaxADC() != a2MinADC ? calibration.getA2MaxADC() : a2MinADC + 1;
			int a3MinADC = calibration.getA3MinADC();
			int a3MaxADC = calibration.getA3MaxADC() != a3MinADC ? calibration.getA3MaxADC() : a3MinADC + 1;
			String calibrationName = calibration.getName();
			
			// Find the scalar and bias by using Cramer's rule for solving Ax = b
			float a0Scalar = (a0MaxValue - a0MinValue)/(a0MaxADC - a0MinADC);
			float a0Bias = a0MaxValue - a0MaxADC * a0Scalar;
			float a1Scalar = (a1MaxValue - a1MinValue)/(a1MaxADC - a1MinADC);
			float a1Bias = a1MaxValue - a1MaxADC * a1Scalar;
			float a2Scalar = (a2MaxValue - a2MinValue)/(a2MaxADC - a2MinADC);
			float a2Bias = a2MaxValue - a2MaxADC * a2Scalar;
			float a3Scalar = (a3MaxValue - a3MinValue)/(a3MaxADC - a3MinADC);
			float a3Bias = a3MaxValue - a3MaxADC * a3Scalar;
			float sa0Scalar = (sa0MaxValue - sa0MinValue)/(sa0MaxADC - sa0MinADC);
			float sa0Bias = sa0MaxValue - sa0MaxADC * sa0Scalar;
			float sa1Scalar = (sa1MaxValue - sa1MinValue)/(sa1MaxADC - sa1MinADC);
			float sa1Bias = sa1MaxValue - sa1MaxADC * sa1Scalar;
			float sa1dScalar = (sa1dMaxValue - sa1dMinValue)/(sa1dMaxADC - sa1dMinADC);
			float sa1dBias = sa1dMaxValue - sa1dMaxADC * sa1dScalar;
			float sa2dScalar = (sa2dMaxValue - sa2dMinValue)/(sa2dMaxADC - sa2dMinADC);
			float sa2dBias = sa2dMaxValue - sa2dMaxADC * sa2dScalar;
			float sa3dScalar = (sa3dMaxValue - sa3dMinValue)/(sa3dMaxADC - sa3dMinADC);
			float sa3dBias = sa3dMaxValue - sa3dMaxADC * sa3dScalar;
			
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
				float a0 = a0Scalar * ControlThread.ADC[3] + a0Bias;
				float a1 = a1Scalar * ControlThread.ADC[2] + a1Bias;
				float a2 = a2Scalar * ControlThread.ADC[0] + a2Bias;
				float a3 = a3Scalar * ControlThread.ADC[1] + a3Bias;
				float sa0 = sa0Scalar * (ControlThread.SDADC[0] + BIT_15) + sa0Bias; // This will turn -32768 to 0 if slope is 1 and bias is 0
				float sa1 = sa1Scalar * (ControlThread.SDADC[1] + BIT_15) + sa1Bias;
				float sa1d = sa1dScalar * ControlThread.DSDADC[0] + sa1dBias;
				float sa2d = sa2dScalar * ControlThread.DSDADC[1] + sa2dBias;
				float sa3d = sa3dScalar * ControlThread.DSDADC[2] + sa3dBias;
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
				Data dataLogg = new Data(0, jobName, sensorName, calibrationName, LocalDateTime.now(), sa0, sa1, sa1d, sa2d, sa3d, a0, a1, a2, a3, di0, di1, di2, di3, di4, di5, p0, p1, p2, p3, p4, p5, p6, p7, p8, d0, d1, d2, pulseNumber, selectedBreakPulseLimit, stopSignal);
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
				boolean breakThreshHold0 = (a0 > a0MaxBreak) || (a0 < a0MinBreak);
				boolean breakThreshHold1 = (a1 > a1MaxBreak) || (a1 < a1MinBreak);
				boolean breakThreshHold2 = (a2 > a2MaxBreak) || (a2 < a2MinBreak);
				boolean breakThreshHold3 = (a3 > a3MaxBreak) || (a3 < a3MinBreak);
				boolean breakThreshHold4 = (sa0 > sa0MaxBreak) || (sa0 < sa0MinBreak);
				boolean breakThreshHold5 = (sa1 > sa1MaxBreak) || (sa1 < sa1MinBreak);
				boolean breakThreshHold6 = (sa1d > sa1dMaxBreak) || (sa1d < sa1dMinBreak);
				boolean breakThreshHold7 = (sa2d > sa2dMaxBreak) || (sa2d < sa2dMinBreak);
				boolean breakThreshHold8 = (sa3d > sa3dMaxBreak) || (sa3d < sa3dMinBreak);
				
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