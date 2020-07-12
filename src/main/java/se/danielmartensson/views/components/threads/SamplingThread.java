package se.danielmartensson.views.components.threads;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import com.github.appreciated.apexcharts.ApexCharts;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.IntegerField;

import se.danielmartensson.views.ControlView;
import se.danielmartensson.views.MySQLView;
import se.danielmartensson.views.components.Mail;
import se.danielmartensson.views.components.PaperSlider;
import se.danielmartensson.views.database.alarmlogg.AlarmLogg;
import se.danielmartensson.views.database.alarmlogg.AlarmLoggRepository;
import se.danielmartensson.views.database.calibrationlogg.CalibrationLogg;
import se.danielmartensson.views.database.calibrationlogg.CalibrationLoggRepository;
import se.danielmartensson.views.database.datalogg.DataLogg;
import se.danielmartensson.views.database.datalogg.DataLoggRepository;

public class SamplingThread extends Thread{
	// For the settings
	private UI ui;
	private DataLoggRepository dataLoggRepository;
	private CalibrationLoggRepository calibrationLoggRepository;
	private AlarmLoggRepository alarmLoggRepository;
	private Mail mail;

	// For logging values
	private Float[] dataAI0;
	private Float[] dataAI1;
	private Float[] dataAI2;
	private Float[] dataAI3;
	private Float[] dataDO0;
	private Float[] dataDO1;
	private Float[] dataDO2;
	private Float[] dataDO3;
	private DateTimeFormatter dtf;
	private static boolean pastPulse;
	static private int pulseNumber;
	
	// Connection to the view
	private ApexCharts apexChart;
	private IntegerField countedPulses;
	private Button loggingActivate;
	private Select<Long> calibration;
	private Select<Long> alarm;
	private Select<Long> loggerId;
	private PaperSlider do0Slider;
	private PaperSlider do1Slider;
	private PaperSlider do2Slider;
	private PaperSlider do3Slider;
	private IntegerField do0Pulse;
	private IntegerField do1Pulse;
	private IntegerField do2Pulse;
	private IntegerField do3Pulse;
	private IntegerField do0Period;
	private IntegerField do1Period;
	private IntegerField do2Period;
	private IntegerField do3Period;
	private Select<Integer> samplingTime;
	private Select<Integer> showSamples;
	private Checkbox showPlot;
	private RadioButtonGroup<String> radioGroup;

	public SamplingThread(DataLoggRepository dataLoggRepository, CalibrationLoggRepository calibrationLoggRepository, AlarmLoggRepository alarmLoggRepository, Mail mail) {
		this.dataLoggRepository = dataLoggRepository;
		this.calibrationLoggRepository = calibrationLoggRepository;
		this.alarmLoggRepository = alarmLoggRepository;
		this.mail = mail;
		
		// Time format
		dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss:SSS");  
	}
	
	@Override
	public void run() {
		while(true) {			
			// Wait loop
			while(ControlView.loggingNow.get() == false) {
				try {
					Thread.sleep(1000); 
				} catch (InterruptedException e) {}
			}
			
			// Series for the plot and restore the plot and counting
			int showSamplesValue = ControlView.selectedShowSamples;
			long loggerIdValue = ControlView.selectedLoggerId;
			int samplingTimeValue = ControlView.selectedSamplingTime;
			int breakPulseLimit = ControlView.selectedBreakPulseLimit;
			dataAI0 = new Float[showSamplesValue];
			dataAI1 = new Float[showSamplesValue];
			dataAI2 = new Float[showSamplesValue];
			dataAI3 = new Float[showSamplesValue];
			dataDO0 = new Float[showSamplesValue];
			dataDO1 = new Float[showSamplesValue];
			dataDO2 = new Float[showSamplesValue];
			dataDO3 = new Float[showSamplesValue];
			
			// Reset
			pulseNumber = 0; 
			pastPulse = false; 
			updatePlotAndPulse();
			
			// Get the calibration and alarm
			CalibrationLogg calibrationLogg = calibrationLoggRepository.findByCID(ControlView.selectedCID);			
			float BAI0 = calibrationLogg.getBAI0();
			float SAI0 =calibrationLogg.getSAI0();
			float BAI1 = calibrationLogg.getBAI1();
			float SAI1 =calibrationLogg.getSAI0();
			float BAI2 = calibrationLogg.getBAI2();
			float SAI2 =calibrationLogg.getSAI0();
			float BAI3 = calibrationLogg.getBAI3();
			float SAI3 =calibrationLogg.getSAI0();
			AlarmLogg alarmLogg = alarmLoggRepository.findByAID(ControlView.selectedAID);
			float AI0Max = alarmLogg.getAI0Max();
			float AI0Min = alarmLogg.getAI0Min();
			float AI1Max = alarmLogg.getAI1Max();
			float AI1Min = alarmLogg.getAI1Min();
			float AI2Max = alarmLogg.getAI2Max();
			float AI2Min = alarmLogg.getAI2Min();
			float AI3Max = alarmLogg.getAI3Max();
			float AI3Min = alarmLogg.getAI3Min();
			String email = alarmLogg.getEmail();
			boolean alarmActivated = alarmLogg.isAlarm();
			
			// Sampling loop
			while(ControlView.loggingNow.get() == true) {
				// Command signals to the device
				int DO0 = ControlThread.do0;
				int DO1 = ControlThread.do1;
				int DO2 = ControlThread.do2;
				int DO3 = ControlThread.do3;
				
				// Read the AI signals and pulse signal
				float AI0 = SAI0*ControlThread.ai0 + BAI0;
				float AI1 = SAI1*ControlThread.ai1 + BAI1;
				float AI2 = SAI2*ControlThread.ai2 + BAI2;
				float AI3 = SAI3*ControlThread.ai3 + BAI3;
				
				// Read the pulse signal and count it if...
				if(ControlThread.pulse != pastPulse) {
					pulseNumber++;
					pastPulse = ControlThread.pulse;
				}
				
				// Read the alarm signal
				boolean stopSignal = ControlThread.stopSignal;

				// Save them to the database
				String time = dtf.format(LocalDateTime.now());
				DataLogg dataLogg = new DataLogg(0, time, DO0, DO1, DO2, DO3, AI0, AI1, AI2, AI3, loggerIdValue, samplingTimeValue, pulseNumber, breakPulseLimit, stopSignal);
				dataLoggRepository.save(dataLogg);
				
				// Show the values on the plot - First shift it back 1 step, set the last element and update the plot
				if(ControlView.selectedShowPlot == true) {
					Collections.rotate(Arrays.asList(dataAI0), -1);
					Collections.rotate(Arrays.asList(dataAI1), -1);
					Collections.rotate(Arrays.asList(dataAI2), -1);
					Collections.rotate(Arrays.asList(dataAI3), -1);
					Collections.rotate(Arrays.asList(dataDO0), -1);
					Collections.rotate(Arrays.asList(dataDO1), -1);
					Collections.rotate(Arrays.asList(dataDO2), -1);
					Collections.rotate(Arrays.asList(dataDO3), -1);
					dataAI0[showSamplesValue-1] = AI0;
					dataAI1[showSamplesValue-1] = AI1;
					dataAI2[showSamplesValue-1] = AI2;
					dataAI3[showSamplesValue-1] = AI3;
					dataDO0[showSamplesValue-1] = (float) DO0;
					dataDO1[showSamplesValue-1] = (float) DO1;
					dataDO2[showSamplesValue-1] = (float) DO2;
					dataDO3[showSamplesValue-1] = (float) DO3;
					updatePlotAndPulse();
				}
				
				// If we exceeded number of pulse limit
				if(pulseNumber >= breakPulseLimit) {
					interuptMessage(email, "Number of pulse limits exceeded", alarmActivated);
					break; // Jump out from the while loop and update the ControlView
				}
				
				// If we exceeded the thresholds
				if((AI0 > AI0Max && AI0 < AI0Min) || (AI1 > AI1Max && AI1 < AI1Min) || (AI2 > AI2Max && AI2 < AI2Min) || (AI3 > AI3Max && AI3 < AI3Min)) {
					interuptMessage(email, "Mesurement exceeded the threshold", alarmActivated);
					break;
				}
				
				// If we read true on stop signal
				if(stopSignal == true) {
					interuptMessage(email, "Stop signal activated - System halted", alarmActivated);
					break;
				}
				
				// Wait
				try {
					Thread.sleep(samplingTimeValue); 
				} catch (InterruptedException e) {}
			}	
			
			// This will cause so components will be enabled again - and also the control thread stops!
			ControlView.loggingNow.set(false); // OFF
			updatePlotAndPulse(); 
		}
	}
	
	private void interuptMessage(String email, String message, boolean alarmActivated) {
		if(alarmActivated == true) {
			mail.sendEmail(email, "Open Source Logger Message", message);
		}
	}

	private void updatePlotAndPulse() {
		ui.access(() -> {
			apexChart.updateSeries(
					MySQLView.createSerie(dataAI0, "AI0"),
					MySQLView.createSerie(dataAI1, "AI1"),
					MySQLView.createSerie(dataAI2, "AI2"),
					MySQLView.createSerie(dataAI3, "AI3"),
					MySQLView.createSerie(dataDO0, "DO0"),
					MySQLView.createSerie(dataDO1, "DO1"),
					MySQLView.createSerie(dataDO2, "DO2"),
					MySQLView.createSerie(dataDO3, "DO3"));
			countedPulses.setValue(pulseNumber);
			disableOrEnableComponents();
			
		});

	}

	public void setComponentsToThread(UI ui, ApexCharts apexChart, IntegerField countedPulses, Button loggingActivate, Select<Long> calibration, Select<Long> alarm, Select<Long> loggerId, PaperSlider do0Slider, PaperSlider do1Slider, PaperSlider do2Slider, PaperSlider do3Slider, IntegerField do0Pulse, IntegerField do1Pulse, IntegerField do2Pulse, IntegerField do3Pulse, IntegerField do0Period, IntegerField do1Period, IntegerField do2Period, IntegerField do3Period, Select<Integer> samplingTime, Select<Integer> showSamples, Checkbox showPlot, RadioButtonGroup<String> radioGroup) {
		this.ui = ui;
		this.apexChart = apexChart;
		this.countedPulses = countedPulses;
		this.loggingActivate = loggingActivate;
		this.calibration = calibration;
		this.alarm = alarm;
		this.loggerId = loggerId;
		this.do0Slider = do0Slider;
		this.do1Slider = do1Slider;
		this.do2Slider = do2Slider;
		this.do3Slider = do3Slider;
		this.do0Pulse = do0Pulse;
		this.do1Pulse = do1Pulse;
		this.do2Pulse = do2Pulse;
		this.do3Pulse = do3Pulse;
		this.do0Period = do0Period;
		this.do1Period = do1Period;
		this.do2Period = do2Period;
		this.do3Period = do3Period;
		this.samplingTime = samplingTime;
		this.showSamples = showSamples;
		this.showPlot = showPlot;
		this.radioGroup = radioGroup;
	}
	
	private void disableOrEnableComponents() {
		if(ControlView.loggingNow.get() == true) {
			// This runs when we don't run the logging
			loggingActivate.setText(ControlView.STOP_LOGGING);
			calibration.setEnabled(false);
			alarm.setEnabled(false);
			loggerId.setEnabled(false);
			do0Slider.setEnabled(true);
			do1Slider.setEnabled(true);
			do2Slider.setEnabled(true);
			do3Slider.setEnabled(true);
			do0Pulse.setEnabled(true);
			do1Pulse.setEnabled(true);
			do2Pulse.setEnabled(true);
			do3Pulse.setEnabled(true);
			do0Period.setEnabled(true);
			do1Period.setEnabled(true);
			do2Period.setEnabled(true);
			do3Period.setEnabled(true);
			samplingTime.setEnabled(false);
			showSamples.setEnabled(false);
			showPlot.setEnabled(false);
			radioGroup.setEnabled(false);
		}else{
			loggingActivate.setText(ControlView.START_LOGGING);
			calibration.setEnabled(true);
			alarm.setEnabled(true);
			loggerId.setEnabled(true);
			do0Slider.setEnabled(false);
			do1Slider.setEnabled(false);
			do2Slider.setEnabled(false);
			do3Slider.setEnabled(false);
			do0Pulse.setEnabled(false);
			do1Pulse.setEnabled(false);
			do2Pulse.setEnabled(false);
			do3Pulse.setEnabled(false);
			do0Period.setEnabled(false);
			do1Period.setEnabled(false);
			do2Period.setEnabled(false);
			do3Period.setEnabled(false);
			samplingTime.setEnabled(true);
			showSamples.setEnabled(true);
			showPlot.setEnabled(true);
			radioGroup.setEnabled(true);
			
			// Set zero values
			do0Slider.setValue(0);
			do1Slider.setValue(0);
			do2Slider.setValue(0);
			do3Slider.setValue(0);
			do0Pulse.setValue(0);
			do1Pulse.setValue(0);
			do2Pulse.setValue(0);
			do3Pulse.setValue(0);
			do0Period.setValue(0);
			do1Period.setValue(0);
			do2Period.setValue(0);
			do3Period.setValue(0);
		}
	}
}
